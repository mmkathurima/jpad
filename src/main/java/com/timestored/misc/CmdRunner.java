package com.timestored.misc;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CmdRunner {
    private static final Logger LOG = Logger.getLogger(CmdRunner.class.getName());

    private static final String REG_EXP = "\"(\\\"|[^\"])*?\"|[^ ]+";
    private static final Pattern PATTERN = Pattern.compile("\"(\\\"|[^\"])*?\"|[^ ]+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    public static String run(String[] commands, String[] envp, File dir) throws IOException {
        Process p = Runtime.getRuntime().exec(commands, envp, dir);
        LOG.info("getRuntime().exec " + Arrays.toString(commands));
        return waitGobbleReturn(p);
    }

    private static String waitGobbleReturn(Process p) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        (new Thread(new StreamGobbler(p.getInputStream(), ps))).start();
        (new Thread(new StreamGobbler(p.getErrorStream(), ps))).start();
        try {
            p.waitFor();
            return baos.toString(StandardCharsets.UTF_8);
        } catch (InterruptedException e) {
            LOG.log(Level.SEVERE, "CmdRunner Run Error", e);
        }
        return "";
    }

    public static String run(String[] commands) throws IOException {
        return run(commands, null, null);
    }

    public static String run(String command) throws IOException {
        Process p = Runtime.getRuntime().exec(command, null, null);
        LOG.info("getRuntime().exec " + command);
        return waitGobbleReturn(p);
    }

    public static Process startProc(String command, File dir) throws IOException {
        Process p = Runtime.getRuntime().exec(command, null, dir);
        gobbleStreams(p);
        return p;
    }

    public static Process startProc(String[] commands, String[] envp, File dir) throws IOException {
        Process p = Runtime.getRuntime().exec(commands, envp, dir);
        LOG.info("getRuntime().exec " + Joiner.on(' ').join(commands));
        gobbleStreams(p);
        return p;
    }

    private static void gobbleStreams(Process p) {
        (new Thread(new StreamGobbler(p.getInputStream(), System.out))).start();
        (new Thread(new StreamGobbler(p.getErrorStream(), System.err))).start();
    }

    public static String[] parseCommand(String cmd) {
        if (cmd == null || cmd.length() == 0) {
            return new String[0];
        }

        cmd = cmd.trim();
        Matcher matcher = PATTERN.matcher(cmd);
        List<String> matches = new ArrayList<>();
        while (matcher.find()) {
            String s = matcher.group();
            if (s.length() >= 2) {
                boolean hasQuotes = ((s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"') || (s.charAt(0) == '\'' && s.charAt(s.length() - 1) == '\''));

                if (hasQuotes) {
                    s = s.substring(1, s.length() - 1);
                }
            }
            matches.add(s);
        }
        return matches.toArray(new String[0]);
    }

    private static class StreamGobbler
            implements Runnable {
        private final InputStreamReader isr;
        private final PrintStream ps;

        public StreamGobbler(InputStream is, PrintStream outputPS) {
            this.isr = new InputStreamReader(is);
            this.ps = Preconditions.checkNotNull(outputPS);
        }

        public void run() {
            try {
                BufferedReader br = new BufferedReader(this.isr);
                String line;
                while ((line = br.readLine()) != null) {
                    this.ps.println(line);
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\misc\CmdRunner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */