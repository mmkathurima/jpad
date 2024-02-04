package com.timestored.sqldash.stockdb;

import com.google.common.base.Preconditions;
import com.timestored.swingxx.ScrollingTextArea;
import com.timestored.theme.Theme;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FinanceDemoFrame
        extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final Dimension SIZE = new Dimension(500, 300);

    private final ScrollingTextArea sTextArea;

    private final FinanceDataDemo fdDemo;

    public FinanceDemoFrame(String title, FinanceDataDemo financeDataDemo) {
        super("Stock Database Demo Runner");
        this.setIconImage(Theme.CIcon.SERVER_GO.getBufferedImage());

        this.fdDemo = Preconditions.checkNotNull(financeDataDemo);

        Container cp = this.getContentPane();
        cp.setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBorder(BorderFactory.createTitledBorder("Server"));
        JLabel lbl = new JLabel(title);
        titlePanel.add(lbl, "North");
        JButton stopAndCloseButton = new JButton("Stop and Close");
        stopAndCloseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FinanceDemoFrame.this.close();
            }
        });
        titlePanel.add(stopAndCloseButton, "South");

        this.sTextArea = new ScrollingTextArea(Color.BLACK, Color.WHITE);

        cp.add(titlePanel, "North");
        cp.add(this.sTextArea, "Center");

        cp.setPreferredSize(SIZE);
        cp.setMinimumSize(SIZE);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                FinanceDemoFrame.this.close();
            }
        });

        financeDataDemo.setListener(FinanceDemoFrame.this.sTextArea::appendMessage);
        this.pack();
    }

    private void close() {
        if (!this.fdDemo.isStopped()) {
            this.fdDemo.stop();
        }
        this.fdDemo.setListener(null);
        this.setVisible(false);
        this.dispose();
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\stockdb\FinanceDemoFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */