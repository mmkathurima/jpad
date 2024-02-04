package io.jpad;

import com.google.common.io.Files;
import com.timestored.sqldash.ChartParams;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JPadParams {
    private static final OptionParser parser = getParser();
    final boolean version;
    final boolean debug;
    final boolean help;
    final String code;
    final File file;
    final String[] programArgs;

    JPadParams(boolean version, boolean debug, boolean help, String code, File file, String[] programArgs) {
        this.version = version;
        this.debug = debug;
        this.help = help;
        this.code = code;
        this.file = file;
        this.programArgs = programArgs;
    }

    private static OptionParser getParser() {
        OptionParser p = new OptionParser();

        p.acceptsAll(List.of(new String[]{"e", "execute"}), "Execute the selected code statement.")
                .withRequiredArg().describedAs("java_code").ofType(String.class);

        p.acceptsAll(List.of(new String[]{"args"}), "Anything after this flag is treated as an argument to the program itself")
                .withRequiredArg().describedAs("program_arguments").ofType(String.class);

        p.accepts("version", "displays version information and causes all other arguments to be ignored");

        p.acceptsAll(List.of(new String[]{"?", "help"}), "Display a help message and exits.").forHelp();

        p.allowsUnrecognizedOptions();
        return p;
    }

    public static JPadParams parse(String... args) throws IOException {
        if (args.length < 1) {
            throw new IllegalArgumentException("Must specify at least one argument to supply code");
        }

        JPadParamsBuilder jpBuilder = (new JPadParamsBuilder()).version(false);

        List<String> argList = Arrays.asList(args);
        int argPos = argList.indexOf("-args");
        String[] programArgs = new String[0];
        String[] jpadArgs = args;
        if (argPos != -1) {
            jpadArgs = argList.subList(0, argPos).toArray(new String[0]);
            programArgs = argList.subList(argPos + 1, argList.size()).toArray(programArgs);
        }

        jpBuilder.programArgs(programArgs);

        OptionSet o = parser.parse(jpadArgs);

        if (o.has("version")) {
            return jpBuilder.version(true).build();
        }
        if (o.has("?")) {
            return jpBuilder.help(true).build();
        }

        String code = null;
        if (o.has("execute")) {
            code = "" + o.valueOf("execute");
        } else if (args.length > 0 && args[0] != null) {
            File file = new File(args[0]);
            if (file.exists() && file.isFile() && file.canRead()) {
                code = Files.toString(file, Charset.defaultCharset());
            }
        }
        if (code == null) {
            code = ChartParams.readConsole();
        }

        return jpBuilder.code(code).build();
    }

    public static void printHelpOn(PrintStream out) throws IOException {
        out.println("Example Call                    What it does");
        out.println("------------                    ------------");
        out.println("jpad                            Start REPL");
        out.println("jpad file.jpad --args blah      Run jpad file with args");
        out.println("jpad --args blah < file.jpad    Run jpad file with args");
        out.println();
        parser.printHelpOn(out);
    }

    static String[] parseCommand(String cmd) {
        if (cmd == null || cmd.length() == 0) {
            return new String[0];
        }

        cmd = cmd.trim();
        final String regExp = "\"(\\\"|[^\"])*?\"|[^ ]+";
        Pattern pattern = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

        Matcher matcher = pattern.matcher(cmd);
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

    public static JPadParamsBuilder builder() {
        return new JPadParamsBuilder();
    }

    boolean isValidFileSpecified() {
        return (this.file != null && this.file.exists() && this.file.isFile() && this.file.canRead());
    }

    private static class JPadParamsBuilder {
        private boolean version;
        private boolean debug;
        private boolean help;
        private String code;
        private File file;
        private String[] programArgs;

        public JPadParamsBuilder version(boolean version) {
            this.version = version;
            return this;
        }

        public JPadParamsBuilder debug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public JPadParamsBuilder help(boolean help) {
            this.help = help;
            return this;
        }

        public JPadParamsBuilder code(String code) {
            this.code = code;
            return this;
        }

        public JPadParamsBuilder file(File file) {
            this.file = file;
            return this;
        }

        public void programArgs(String[] programArgs) {
            this.programArgs = programArgs;
        }

        public JPadParams build() {
            return new JPadParams(this.version, this.debug, this.help, this.code, this.file, this.programArgs);
        }

        public String toString() {
            return "JPadParams.JPadParamsBuilder(version=" + this.version + ", debug=" + this.debug + ", help=" + this.help + ", code=" + this.code + ", file=" + this.file + ", programArgs=" + Arrays.deepToString(this.programArgs) + ")";
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\JPadParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */