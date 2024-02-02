package com.timestored.swingxx;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseWheelListener;


public class ScrollingTextArea
        extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_MAX_LENGTH = 8000;
    private final JTextArea textArea;
    private final JScrollPane scrollpane;
    private int maxLength = 8000;


    public ScrollingTextArea(Color fgColor, Color bgColor) {
        setLayout(new BorderLayout());
        this.textArea = new JTextArea();
        this.textArea.setName("consolePanel-textArea");
        this.textArea.setEditable(false);
        this.textArea.setBackground(bgColor);
        DefaultCaret caret = (DefaultCaret) this.textArea.getCaret();
        caret.setUpdatePolicy(2);
        this.textArea.setForeground(fgColor);
        this.scrollpane = new JScrollPane(this.textArea);
        add(this.scrollpane, "Center");
    }

    public Font getTextareaFont() {
        return this.textArea.getFont();
    }

    public void setTextareaFont(Font f) {
        this.textArea.setFont(f);
    }

    public String getText() {
        return this.textArea.getText();
    }


    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public void clear() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                ScrollingTextArea.this.textArea.setText("");
            }
        });
    }

    public synchronized void addMouseWheelListener(MouseWheelListener l) {
        this.textArea.addMouseWheelListener(l);
    }

    public void appendMessage(final String msg) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                String t = ScrollingTextArea.this.textArea.getText();
                if (t.length() > ScrollingTextArea.this.maxLength) {
                    int startOffset = Math.abs(ScrollingTextArea.this.maxLength - t.length());
                    ScrollingTextArea.this.textArea.setText(t.substring(startOffset));
                }
                ScrollingTextArea.this.textArea.append(msg + (msg.endsWith("\n") ? "" : "\r\n"));

                ScrollingTextArea.this.textArea.revalidate();
                ScrollingTextArea.this.textArea.setCaretPosition(ScrollingTextArea.this.textArea.getDocument().getLength());
                JScrollBar vertical = ScrollingTextArea.this.scrollpane.getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
            }
        });
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\swingxx\ScrollingTextArea.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */