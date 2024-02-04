package com.timestored.command;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.timestored.swingxx.SwingUtils;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class CommandDialog
        extends JDialog {
    private static final long serialVersionUID = 1L;
    private static final Dimension PREF_DIMENSION = new Dimension(300, 400);
    private static final int RESULT_LIMIT = 100;
    private final CommandPanel commandPanel;
    private final ExecutorService executorService;
    private String prevSearch = "";
    private Collection<Command> commands = Collections.emptyList();

    public CommandDialog(String title, List<Command> commands, ExecutorService executorService) {
        this(title, new CommandProvider() {
            public Collection<Command> getCommands() {
                return commands;
            }
        }, executorService);
    }

    public CommandDialog(String title, CommandProvider commandProvider, ExecutorService executorService) {
        this.executorService = Preconditions.checkNotNull(executorService);
        this.setTitle(title);
        this.setName("CodeOutlineDialog");
        this.setMinimumSize(PREF_DIMENSION);
        this.setPreferredSize(PREF_DIMENSION);

        this.commandPanel = new CommandPanel();
        JTextField searchTextField = new JTextField();

        ActionMap am = searchTextField.getActionMap();
        InputMap im = searchTextField.getInputMap();
        am.put("upAction", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                CommandDialog.this.commandPanel.moveUp();
            }
        });
        im.put(KeyStroke.getKeyStroke("UP"), "upAction");

        am.put("downAction", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                CommandDialog.this.commandPanel.moveDown();
            }
        });
        im.put(KeyStroke.getKeyStroke("DOWN"), "downAction");

        am.put("escapeAction", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                CommandDialog.this.dispose();
            }
        });
        im.put(SwingUtils.ESC_KEYSTROKE, "escapeAction");

        searchTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Command com = CommandDialog.this.commandPanel.getSelectedCommand();
                if (com != null) {
                    com.perform();
                }
                CommandDialog.this.dispose();
            }
        });

        Utils.addEscapeCloseListener(this);
        Utils.putEscapeAction(searchTextField, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                CommandDialog.this.dispose();
            }
        });

        this.commandPanel.setSelectAction(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                Command com = CommandDialog.this.commandPanel.getSelectedCommand();
                if (com != null) {
                    com.perform();
                }
                CommandDialog.this.dispose();
            }
        });

        this.addWindowFocusListener(new WindowFocusListener() {
                                        public void windowLostFocus(WindowEvent e) {
                                            CommandDialog.this.dispose();
                                        }

                                        public void windowGainedFocus(WindowEvent e) {
                                        }
                                    }
        );
        searchTextField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                KeyStroke ks = KeyStroke.getKeyStroke(e.getKeyCode(), e.getModifiers());
                if (!ks.equals(KeyStroke.getKeyStroke("UP")) && !ks.equals(KeyStroke.getKeyStroke("DOWN"))) {

                    String txt = searchTextField.getText();
                    if (!txt.equals(CommandDialog.this.prevSearch)) {
                        CommandDialog.this.showDocsForSearch(txt);
                        CommandDialog.this.prevSearch = txt;
                    }
                }
            }
        });

        this.setLayout(new BorderLayout());
        this.add(searchTextField, "North");
        this.add(this.commandPanel, "Center");
        searchTextField.requestFocus();

        this.pack();

        this.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent e) {
                CommandDialog.this.setVisible(false);
                CommandDialog.this.dispose();
            }

            public void focusGained(FocusEvent e) {
            }
        });
        executorService.execute(new Runnable() {
            public void run() {
                CommandDialog.this.commands = commandProvider.getCommands();
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        CommandDialog.this.showDocsForSearch(CommandDialog.this.prevSearch);
                    }
                });
            }
        });
    }

    private void showDocsForSearch(String txt) {
        if (txt.trim().length() > 0) {
            String[] t = txt.trim().split(" ");
            List<Command> r = new ArrayList<>();
            for (Command c : this.commands) {
                boolean match = true;
                if (t.length > 0) {
                    for (String s : t) {
                        if (!c.getTitle().toUpperCase().contains(s.toUpperCase())) {
                            match = false;
                            break;
                        }
                    }
                }
                if (match) {
                    r.add(c);
                }
                if (r.size() > 100) {
                    break;
                }
            }
            this.commandPanel.setCommands(r);
        } else {
            List<Command> l = Lists.newArrayList(this.commands);
            if (l.size() > 100) {
                l = l.subList(0, 100);
            }
            this.commandPanel.setCommands(l);
        }
    }

    public void setSelectedCommand(Command command) {
        this.commandPanel.setSelectedCommand(command);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\command\CommandDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */