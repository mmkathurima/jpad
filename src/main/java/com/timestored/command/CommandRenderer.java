package com.timestored.command;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;


class CommandRenderer
        extends JLabel
        implements ListCellRenderer {
    private static final CommandRenderer INSTANCE = new CommandRenderer();

    private static final long serialVersionUID = 1L;

    public static CommandRenderer getInstance() {
        return INSTANCE;
    }


    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(this, "West");


        if (value instanceof Command) {
            Command c = (Command) value;

            if (c.getIcon() != null) {
                setIcon(c.getIcon());
            }


            String t = c.getTitle();
            String ta = c.getTitleAdditional();
            if (ta != null && ta.length() > 0 && ta.length() < 60) {
                t = "<html>" + c.getTitle() + "<font color='#666666'> - " + ta + "</font></html>";
            }
            setText(t);

            KeyStroke ks = c.getKeyStroke();
            if (ks != null) {
                String s = ks.toString().replace("pressed ", "").replace("ctrl", "Ctrl").replace("shift", "Shift");

                JLabel l = new JLabel(s);
                l.setForeground(Color.DARK_GRAY);
                p.add(l, "East");
            }
        } else {

            setText(value.toString());
        }


        if (isSelected) {
            p.setBackground(list.getSelectionBackground());
            p.setForeground(list.getSelectionForeground());
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            p.setBackground(list.getBackground());
            p.setForeground(list.getForeground());
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setOpaque(true);

        return p;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\command\CommandRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */