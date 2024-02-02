package io.jpad;

import io.jpad.model.JEngine;
import io.jpad.model.RunConfig;
import io.jpad.model.RunResult;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Handler;
import java.util.logging.Logger;


public class JPad {
    public static void main(String... args) throws IOException {
        boolean debug = Arrays.asList(args).contains("-debug");

        if (!debug) {

            Logger globalLogger = Logger.getLogger("");
            Handler[] handlers = globalLogger.getHandlers();
            for (Handler handler : handlers) {
                globalLogger.removeHandler(handler);
            }
        }

        if (args.length == 0) {
            (new REPL()).run();
        } else {
            System.exit(run(args));
        }
    }

    private static int run(String... args) throws IOException {
        int ret = 0;
        JPadParams jPadParams = JPadParams.parse(args);
        if (jPadParams.version) {
            System.out.println("http://jpad.io version = 1.07");
        } else if (jPadParams.help) {
            JPadParams.printHelpOn(System.out);
        } else {
            RunResult rr = runWaitReturn(new RunConfig(jPadParams.code, jPadParams.programArgs));
            if (rr.isErrorOccurred()) {
                System.err.println(rr.getError());
            }
            ret = rr.getExitCode();
        }
        return ret;
    }


    public static int testGenerate(String arg) throws IOException {
        return run(JPadParams.parseCommand(arg));
    }

    public static RunResult runWaitReturn(RunConfig runConfig) {
        JEngine jEngine = new JEngine();
        return jEngine.runWaitReturn(runConfig);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\JPad.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */