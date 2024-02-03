package io.jpad.model;

import com.timestored.misc.IOUtils;
import io.jpad.scratch.Dumper;

import javax.tools.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JEngine {
    public static final String CLASS_NAME = "io.jpad.scratch.RunnContainer";
    public static final String CLASS_PATH = "io/jpad/scratch/RunnContainer.java";
    private static final Logger LOG = Logger.getLogger(JEngine.class.getName());
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(1);
    private final List<JEngineListener> listeners = new CopyOnWriteArrayList<>();
    private final Map<String, ResultRenderer> renderers = new ConcurrentHashMap<>();

    public void addListener(JEngineListener listener) {
        this.listeners.add(listener);
    }

    public void registerResultRenderer(ResultRenderer resultRenderer) {
        String name = resultRenderer.getTabName();
        this.addListener(resultRenderer);
        if (this.renderers.containsKey(name)) {
            LOG.log(Level.SEVERE, "renderer with name: " + name + " was previously added. Overriding now.");
        }
        this.renderers.put(name, resultRenderer);
    }

    void run(String code) {
        this.run(new RunConfig(code));
    }

    public void run(RunConfig runConfig) {
        Runnable r = new Runnable() {
            public void run() {
                JEngine.this.runWaitReturn(runConfig);
            }
        };
        EXECUTOR.execute(r);
    }

    public RunResult runWaitReturn(RunConfig runConfig) {
        RunResult runResult = new RunResult(runConfig);
        JPadCode jCode = JPadCode.generate(runConfig.getRawCode());
        runResult.setJPadCode(jCode);
        try {
            this.runn(jCode, runConfig, runResult);
        } catch (IllegalStateException | IllegalArgumentException ise) {
            runResult.setError(ise.getMessage());
        }
        LOG.info("runResult = " + runResult.toString().replace("\r", ""));
        for (JEngineListener l : this.listeners) l.resultReturned(runResult);
        return runResult;
    }

    private void runn(JPadCode code, RunConfig runConfig, RunResult runResult) {
        File base = null;
        try {
            base = Files.createTempDirectory("").toFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!code.getGradleCode().isEmpty()) ;
        File root = new File(base, "src/main/java");
        LOG.info("root = " + root.getAbsolutePath());
        File sourceFile = new File(root, "io/jpad/scratch/RunnContainer.java");
        sourceFile.getParentFile().mkdirs();
        try {
            (new FileWriter(sourceFile)).append(code.getGeneratedCode()).close();
        } catch (IOException e) {
            final String msg = "Internal Engine Error could not find source file";
            LOG.log(Level.SEVERE, msg, e);
            throw new IllegalStateException(msg);
        }
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("jpad-err-jdk-1 - Could not find java compiler, you must be using a JDK");
        }
        DiagnosticCollector<JavaFileObject> diagnosticsCollector = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnosticsCollector, null, null);
        for (JEngineListener l : this.listeners) l.compiling(code);
        StringWriter writer = new StringWriter();
        JavaCompiler.CompilationTask compileTask = compiler.getTask(writer, fileManager, diagnosticsCollector, null, null, fileManager
                .getJavaFileObjects(sourceFile));
        ExecutorService service = Executors.newFixedThreadPool(20);
        Future<Boolean> result = service.submit(compileTask);
        boolean compileSuccessful = false;
        try {
            compileSuccessful = result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        String javapOutput = "";
        if (compileSuccessful) {
            try {
                StringWriter sw = new StringWriter();
                //Main.compile(new String[]{"-cp", root.getAbsolutePath(), "-c", "-p", "-constants", "io.jpad.scratch.RunnContainer"}, new PrintWriter(sw));
                javapOutput = sw.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        runResult.setCompileResult(compileSuccessful, writer.toString(), diagnosticsCollector.getDiagnostics(), javapOutput);
        for (JEngineListener l : this.listeners) {
            l.compiled(runResult);
        }
        if (!compileSuccessful) {
            throw new IllegalArgumentException("Compilation Problem");
        }
        try {
            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{root.toURI().toURL()});
            Class<?> cls = Class.forName("io.jpad.scratch.RunnContainer", true, classLoader);
            runResult.setRunning();
            for (JEngineListener l : this.listeners) l.running(code);
            Dumper.captureSystemOutput();
            Object runn = cls.newInstance();
            try {
                Method m = runn.getClass().getMethod("main", String[].class);
                m.invoke(null, new Object[]{runConfig.getArgs()});
            } catch (InvocationTargetException e) {
                Throwable t = e.getCause();
                if (t != null) {
                    runResult.setError(this.toString(t));
                } else {
                    throw new IllegalStateException("Internal Engine Error: problem calling main().", e);
                }
            } catch (NoSuchMethodException | SecurityException | IllegalArgumentException e) {
                throw new IllegalStateException("Internal Engine Error: problem calling main().", e);
            } finally {
                Dumper.restoreSystemOut();
            }
            runResult.setRan(Dumper.getOutput(), Dumper.GetDumps());
            Dumper.Clear();
        } catch (MalformedURLException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            throw new IllegalStateException("Internal Engine Error could not find source file", e);
        }
    }

    private String toString(Throwable t) {
        if (t != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(t.getClass()).append(":").append(t.getLocalizedMessage()).append("\r\n");
            StackTraceElement[] st = t.getStackTrace();
            for (int i = 0; i < st.length && st[i] != null; i++) {
                StackTraceElement el = st[i];
                boolean containerLevel = el.getClassName().equals("io.jpad.scratch.RunnContainer");
                sb.append("\tat ");
                if (!containerLevel) {
                    sb.append(el.getClassName());
                }
                sb.append("." + el.getMethodName() + "(");
                if (!containerLevel) {
                    sb.append(el.getFileName());
                }
                sb.append(":" + el.getLineNumber() + ")").append("\r\n");
                if (containerLevel) {
                    break;
                }
            }
            return sb.toString();
        }
        return "";
    }

    public void runJPadsFromFolder(File dir) {
        if (dir.exists() && dir.isDirectory()) {
            try {
                Files.walk(dir.toPath(), new java.nio.file.FileVisitOption[0])
                        .filter(fn -> fn.getFileName().toString().endsWith(".jpad"))
                        .forEach(new Consumer<Path>() {
                            public void accept(Path pth) {
                                try {
                                    RunConfig rc = new RunConfig(IOUtils.toString(pth.toFile()));
                                    JEngine.this.runWaitReturn(rc);
                                } catch (IOException e) {
                                    LOG.log(Level.WARNING, "Error running file: " + pth, e);
                                }
                            }
                        });
            } catch (IOException e) {
                LOG.log(Level.WARNING, "Error running jpad files", e);
            }
        }
    }

    public Map<String, String> getLatestRenderings() {
        Map<String, String> m = new HashMap<>();
        for (Map.Entry<String, ResultRenderer> e : this.renderers.entrySet()) {
            m.put(e.getKey(), e.getValue().getLatestRendering());
        }
        return m;
    }
}
/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\model\JEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */