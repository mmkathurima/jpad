package com.timestored.sqldash;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;


class ButtonTabComponent
        extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final MouseListener buttonMouseListener = new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(true);
            }
        }


        public void mouseExited(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(false);
            }
        }
    };
    private final JTabbedPane pane;

    public ButtonTabComponent(final JTabbedPane pane) {
        super(new FlowLayout(0, 0, 0));
        if (pane == null) {
            throw new NullPointerException("TabbedPane is null");
        }
        this.pane = pane;
        setOpaque(false);


        JLabel label = new JLabel() {
            private static final long serialVersionUID = 1L;


            public String getText() {
                int i = pane.indexOfTabComponent(ButtonTabComponent.this);
                if (i != -1) {
                    return pane.getTitleAt(i);
                }
                return null;
            }
        };

        add(label);

        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

        JButton button = new TabButton();
        add(button);

        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
    }

    private class TabButton
            extends JButton implements ActionListener {
        private static final long serialVersionUID = 1L;

        public TabButton() {
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("close this tab");

            setUI(new BasicButtonUI());

            setContentAreaFilled(false);

            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);


            addMouseListener(ButtonTabComponent.buttonMouseListener);
            setRolloverEnabled(true);

            addActionListener(this);
        }


        public void actionPerformed(ActionEvent e) {
            int i = ButtonTabComponent.this.pane.indexOfTabComponent(ButtonTabComponent.this);
            if (i != -1) {
                ButtonTabComponent.this.pane.remove(i);
            }
        }


        public void updateUI() {
        }


        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();

            if (getModel().isPressed()) {
                g2.translate(1, 1);
            }
            g2.setStroke(new BasicStroke(2.0F));
            g2.setColor(Color.BLACK);
            if (getModel().isRollover()) {
                g2.setColor(Color.MAGENTA);
            }
            int delta = 6;
            g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
            g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
            g2.dispose();
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\ButtonTabComponent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */