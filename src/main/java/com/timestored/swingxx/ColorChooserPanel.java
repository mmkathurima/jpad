package com.timestored.swingxx;

import javax.swing.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ColorChooserPanel
        extends JPanel {
    private static final long serialVersionUID = 1L;
    private final JLabel colorPreviewLabel = new JLabel("       ");

    private final JButton colorButton;

    public ColorChooserPanel(Component parent) {
        this.colorPreviewLabel.setOpaque(true);
        this.colorPreviewLabel.setBorder(BorderFactory.createLoweredBevelBorder());

        this.colorButton = new JButton("Choose Color");
        this.colorPreviewLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Color c = JColorChooser.showDialog(parent, "Choose Color", ColorChooserPanel.this.colorButton.getBackground());

                ColorChooserPanel.this.colorPreviewLabel.setBackground(c);
                ColorChooserPanel.this.colorButton.setBackground(c);
            }
        });
        this.colorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                Color c = JColorChooser.showDialog(parent, "Choose Color", ColorChooserPanel.this.colorButton.getBackground());

                ColorChooserPanel.this.colorPreviewLabel.setBackground(c);
                ColorChooserPanel.this.colorButton.setBackground(c);
            }
        });

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(this.colorButton);
        p.add(this.colorPreviewLabel);
        this.add(p);
    }

    public Color getColor() {
        return this.colorButton.getBackground();
    }

    public void setColor(Color color) {
        this.colorButton.setBackground(color);
        this.colorPreviewLabel.setBackground(color);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\swingxx\ColorChooserPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */