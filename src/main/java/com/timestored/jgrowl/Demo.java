package com.timestored.jgrowl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

public class Demo
        implements Runnable {
    private Growler growler;
    private JTextArea textArea;
    private JTextField titleField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Demo());
    }

    public void run() {
        JFrame frame = new JFrame("JGrowl Demo");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(640, 480));
        Container c = frame.getContentPane();
        c.setLayout(new BorderLayout(5, 5));

        this.growler = GrowlerFactory.getGrowler(frame);
        this.textArea = new JTextArea("Hello World! This is my message");
        this.titleField = new JTextField("growler title");

        JPanel buttonPanel = new JPanel(new GridLayout(1, 0));
        buttonPanel.add(this.getButton(Level.INFO));
        buttonPanel.add(this.getButton(Level.WARNING));
        buttonPanel.add(this.getButton(Level.SEVERE));

        JPanel p = new JPanel(new GridLayout(0, 1));
        p.add(buttonPanel);
        p.add(this.titleField);
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(p);
        c.add(panel, "North");
        c.add(this.textArea, "Center");

        frame.pack();
        frame.setVisible(true);
    }

    private JButton getButton(Level l) {
        JButton addButton = new JButton(l.getName());
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Demo.this.growler.show(l, Demo.this.textArea.getText(), Demo.this.titleField.getText());
            }
        });
        return addButton;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\jgrowl\Demo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */