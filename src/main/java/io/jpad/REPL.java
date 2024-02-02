package io.jpad;

import com.google.common.collect.Lists;
import io.jpad.model.JEngine;
import io.jpad.model.RunConfig;
import io.jpad.model.RunResult;

import java.util.ArrayList;
import java.util.Scanner;


class REPL {
    private static final String PROMPT = "j>";
    private final ArrayList<String> history = Lists.newArrayList();
    private final JEngine jEngine = new JEngine();
    private boolean debugMode = false;

    public REPL() {
        this.jEngine.runJPadsFromFolder(JPadConfig.PLUGINS_FOLDER);
    }


    public void run() {
        displayLogo();
        displayHelp();

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("j>");
            while (scanner.hasNextLine()) {
                String s = scanner.nextLine();

                if (s.startsWith("\\")) {
                    String slashCmd = s;
                    if (s.indexOf(" ") != -1) {
                        slashCmd = s.substring(0, s.indexOf(" "));
                    }

                    if (slashCmd.equals("\\")) {
                        this.debugMode = !this.debugMode;
                    } else if (slashCmd.equals("\\exit") || slashCmd.equals("\\\\")) {
                        System.exit(0);
                    } else if (slashCmd.equals("\\help") || slashCmd.equals("\\?")) {
                        displayHelp();
                    } else if (slashCmd.equals("\\clear")) {
                        String[] st = s.split(" ");
                        if (st.length < 2) {
                            this.history.clear();
                        } else {
                            try {
                                int rowsToRemove = Integer.parseInt(st[1]);
                                for (int i = this.history.size() - 1; i >= 0 && rowsToRemove > 0; i--, rowsToRemove--) {
                                    this.history.remove(i - 1);
                                }
                            } catch (NumberFormatException nfe) {
                                System.err.println("Could not understand clear number");
                            }
                        }
                        System.out.println("history cleared");
                    } else if (slashCmd.equals("\\history")) {
                        for (String h : this.history) {
                            System.out.println(h);
                        }
                    }
                } else {

                    String cmd = s;
                    if (cmd.trim().length() > 0) {
                        boolean debugRun = (s.startsWith(">") || !s.trim().endsWith(";") || this.debugMode);
                        if (debugRun) {
                            if (s.startsWith(">")) {
                                cmd = cmd.substring(1);
                            }
                            if (!cmd.contains(";")) {
                                cmd = "Dump(" + cmd + ");";
                            }
                        }
                        this.history.add(cmd);
                        RunResult rr = evalAll();
                        if (rr.isErrorOccurred()) {
                            System.err.println(rr.getError());
                        }


                        if (rr.getExitCode() != 0 || debugRun) {
                            this.history.remove(this.history.size() - 1);
                        }
                    }
                }
                System.out.print("j>" + (this.debugMode ? ">" : ""));
            }
        }
    }


    private void displayLogo() {
        System.out.println();
        System.out.println("       _ _____          _ ");
        System.out.println("      | |  __ \\        | |");
        System.out.println("      | | |__) |_ _  __| |");
        System.out.println("  _   | |  ___/ _` |/ _` |");
        System.out.println(" | |__| | |  | (_| | (_| |");
        System.out.println("  \\____/|_|   \\__,_|\\__,_|");
        System.out.println("                          ");
        System.out.println();
    }

    private void displayHelp() {
        System.out.println();
        System.out.println("Anything you type is evaluated as java.");
        System.out.println("The code is continuously appended until you call \\clear.");
        System.out.println("Other Available Commands:");
        System.out.println("\\exit - exit");
        System.out.println("\\clear (n) - clear past java statements");
        System.out.println("\\history - display all past java statements");
        System.out.println("\\help - display this help");
        System.out.println();
    }


    private synchronized RunResult evalAll() {
        StringBuilder sb = new StringBuilder();
        for (String h : this.history) {
            sb.append(h);
        }
        return this.jEngine.runWaitReturn(new RunConfig(sb.toString()));
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\REPL.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */