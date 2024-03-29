package io.jpad;

import com.google.common.base.Joiner;
import com.timestored.misc.CmdRunner;
import io.jpad.model.RunConfig;
import org.jetbrains.annotations.NotNull;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class ArgsEditorPanel
        extends JPanel {
    private static final long serialVersionUID = 1L;
    private final JTextField textField = new JTextField(20);
    private final RunConfig runConfig;

    public ArgsEditorPanel(@NotNull RunConfig runConfig) {
        if (runConfig == null) throw new NullPointerException("runConfig");

        this.runConfig = runConfig;

        this.setLayout(new BorderLayout());
        this.add(new JLabel("Program Arguments:"), "West");
        this.add(this.textField, "Center");

        this.textField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String[] args = CmdRunner.parseCommand(ArgsEditorPanel.this.textField.getText());
                runConfig.setArgs(args);
            }
        });

        runConfig.addListener(ArgsEditorPanel.this::refresh);
        this.refresh();
    }

    private void refresh() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                String txt = Joiner.on(" ").join(ArgsEditorPanel.this.runConfig.getArgs());
                ArgsEditorPanel.this.textField.setText(txt);
            }
        });
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\ArgsEditorPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */