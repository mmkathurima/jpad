package io.jpad.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.timestored.misc.HtmlUtils;
import io.jpad.scratch.CapturedObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class RunResult {
    private static final char NL = '\n';
    private static final String REPLACEMENT_CLASS_NAME = "YourJpadCode";
    private final RunConfig runConfig;
    private Stage stage = Stage.RAW_CODE;
    private JPadCode jPadCode;
    private boolean compileSuccessful;
    @NotNull
    private String compileResult = "";
    @NotNull
    private String javapOutput;
    @NotNull
    private List<Diagnostic<? extends JavaFileObject>> diagnostics = Collections.emptyList();
    @Nullable
    private String error;

    @NotNull
    private String output = "";
    private List<CapturedObject> dumps = Collections.emptyList();

    RunResult(RunConfig runConfig) {

        this.runConfig = Preconditions.checkNotNull(runConfig);
    }

    public RunConfig getRunConfig() {

        return this.runConfig;
    }

    public boolean isCompileSuccessful() {

        return this.compileSuccessful;
    }

    @NotNull
    public List<Diagnostic<? extends JavaFileObject>> getDiagnostics() {
        return this.diagnostics;
    }

    @NotNull
    public String getJavapOutput() {
        return this.javapOutput;
    }

    @NotNull
    public String getOutput() {
        return this.output;
    }

    public List<CapturedObject> getDumps() {
        return this.dumps;
    }

    public void setJPadCode(JPadCode jPadCode) {

        this.jPadCode = Preconditions.checkNotNull(jPadCode);

        this.setStage(Stage.CONVERTED_TO_JAVA);
    }

    public boolean isErrorOccurred() {
        return (this.error != null);
    }

    public boolean isCompletedOk() {

        return (this.error == null && this.stage.equals(Stage.COMPLETED));
    }

    private void setStage(Stage stage) {

        Preconditions.checkNotNull(stage);

        if (!stage.equals(Stage.COMPLETED) && !stage.isDirectlyAfter(this.stage)) {

            throw new IllegalStateException("Cant move from stage " + this.stage + " to " + stage);
        }

        this.stage = stage;
    }

    RunResult setCompileResult(boolean compileSuccessful, String compileResult, List<Diagnostic<? extends JavaFileObject>> diagnostics, String javapOutput) {

        this.compileSuccessful = compileSuccessful;

        this.javapOutput = (javapOutput == null) ? "" : javapOutput;

        this.compileResult = (compileResult == null) ? "" : compileResult;

        List<Diagnostic<? extends JavaFileObject>> ds = Lists.newArrayList();

        if (diagnostics != null) {

            for (Diagnostic<? extends JavaFileObject> d : diagnostics) {

                ds.add(new WrappedDiagnostic<>(d));
            }
        }

        this.diagnostics = ImmutableList.copyOf(ds);

        this.setStage(Stage.COMPILED);

        return this;
    }

    public void setRunning() {

        this.setStage(Stage.RUNNING);
    }

    public String toString() {

        return "RunResult [RunConfig=" + this.runConfig + ", compileResult=" + this.compileResult + ", error=" + this.error + ", output=" + this.output + ", dumps=" + this.dumps + "]";
    }

    public String toCsv() {

        if (this.isErrorOccurred()) {

            return "ERROR: " + this.error;
        }

        return CsvConverter.convert(this.dumps);
    }

    public String toHtml() {

        if (this.isErrorOccurred()) {

            StringBuilder sb = new StringBuilder();

            sb.append("<h1 class='error'>ERROR:</h1>").append(HtmlUtils.escapeHTML(this.error)).append("<br/>");

            if (!this.diagnostics.isEmpty()) {

                sb.append("<pre><code>");

                sb.append(this.getCompileErrorDescription());

                sb.append("</pre></code>");
            }

            return sb.toString();
        }

        return HtmlConverter.convert(this.dumps);
    }

    @NotNull
    public String getCompileResult() {

        StringBuilder sb = new StringBuilder();

        if (!this.compileResult.isEmpty()) {

            sb.append(this.compileResult).append('\n');
        }

        if (!this.compileSuccessful) {

            sb.append(this.getCompileErrorDescription());
        }

        return sb.toString();
    }

    private String getCompileErrorDescription() {

        StringBuilder sb = new StringBuilder();

        for (Diagnostic<? extends JavaFileObject> d : this.diagnostics) {

            int line = (int) d.getLineNumber();

            sb.append(d.getKind().toString()).append(" at line number:").append(line + 1).append("\n");

            try {

                sb.append(d.getCode().split("\n")[line]);
            } catch (IndexOutOfBoundsException e) {

                sb.append("Could not find line");
            }

            sb.append("\n");

            for (int i = 1; i < d.getColumnNumber(); i++) {

                sb.append("-");
            }

            sb.append("^").append("\n\n");

            String msg = d.getMessage(Locale.getDefault());

            msg = msg.replace("class io.jpad.scratch.RunnContainer", "YourJpadCode");

            msg = msg.replace("io.jpad.scratch.RunnContainer", "YourJpadCode");

            sb.append(msg).append("\n\n\n");
        }

        return sb.toString();
    }

    public void setRan(String output, List<CapturedObject> list) {

        this.output = (output == null) ? "" : output;

        this.dumps = (list == null) ? Collections.emptyList() : list;

        this.setStage(Stage.COMPLETED);
    }

    public String getRawCode() {

        return this.runConfig.getRawCode();
    }

    public int getExitCode() {

        boolean realError = (this.error != null && this.error.trim().length() > 0);

        if (this.stage.equals(Stage.COMPLETED) && !realError) {

            return 0;
        }

        return 1;
    }

    public String getError() {

        if (!this.compileSuccessful) {

            return this.getCompileErrorDescription().trim();
        }

        return this.error;
    }

    public void setError(String error) {

        this.error = (error == null) ? "" : error;

        this.setStage(Stage.COMPLETED);
    }

    public enum Stage {
        RAW_CODE(0), CONVERTED_TO_JAVA(1),

        COMPILED(2),

        RUNNING(3),

        COMPLETED(4);
        private final int n;

        Stage(int n) {

            this.n = n;
        }

        boolean isDirectlyAfter(Stage stage) {
            return (this.n == stage.n + 1);
        }

        boolean isDirectlyBefore(Stage stage) {

            return (this.n == stage.n - 1);
        }
    }

    class WrappedDiagnostic<T extends JavaFileObject> implements Diagnostic<JavaFileObject> {
        private final Diagnostic<? extends JavaFileObject> d;

        public WrappedDiagnostic(Diagnostic<? extends JavaFileObject> diagnostic) {

            this.d = Preconditions.checkNotNull(diagnostic);
        }

        private long trans(long l) {

            try {

                return RunResult.this.jPadCode.translateOffset((int) l);
            } catch (IllegalArgumentException e) {

                return -1L;
            }
        }

        public long getStartPosition() {

            return this.trans(this.d.getStartPosition());
        }

        public long getEndPosition() {

            return this.trans(this.d.getEndPosition());
        }

        public long getPosition() {

            return this.trans(this.d.getPosition());
        }

        public long getLineNumber() {

            int p = (int) this.getPosition();

            if (p != -1L) {

                String s = this.getCode().substring(0, p);

                return (s.length() - s.replace("\n", "").length());
            }

            return -1L;
        }

        public String getCode() {

            return RunResult.this.runConfig.getRawCode();
        }

        public long getColumnNumber() {

            return this.d.getColumnNumber();
        }

        public Diagnostic.Kind getKind() {

            return this.d.getKind();
        }

        public String getMessage(Locale locale) {

            return this.d.getMessage(locale);
        }

        public JavaFileObject getSource() {

            return this.d.getSource();
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\model\RunResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */