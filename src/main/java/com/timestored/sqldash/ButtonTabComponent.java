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

    public ButtonTabComponent(JTabbedPane pane) {
        super(new FlowLayout(0, 0, 0));
        if (pane == null) {
            throw new NullPointerException("TabbedPane is null");
        }
        this.pane = pane;
        this.setOpaque(false);

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

        this.add(label);

        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

        JButton button = new TabButton();
        this.add(button);

        this.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
    }

    private class TabButton
            extends JButton implements ActionListener {
        private static final long serialVersionUID = 1L;

        public TabButton() {
            final int size = 17;
            this.setPreferredSize(new Dimension(size, size));
            this.setToolTipText("close this tab");

            this.setUI(new BasicButtonUI());

            this.setContentAreaFilled(false);

            this.setFocusable(false);
            this.setBorder(BorderFactory.createEtchedBorder());
            this.setBorderPainted(false);

            this.addMouseListener(buttonMouseListener);
            this.setRolloverEnabled(true);

            this.addActionListener(this);
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

            if (this.getModel().isPressed()) {
                g2.translate(1, 1);
            }
            g2.setStroke(new BasicStroke(2.0F));
            g2.setColor(Color.BLACK);
            if (this.getModel().isRollover()) {
                g2.setColor(Color.MAGENTA);
            }
            final int delta = 6;
            g2.drawLine(delta, delta, this.getWidth() - delta - 1, this.getHeight() - delta - 1);
            g2.drawLine(this.getWidth() - delta - 1, delta, delta, this.getHeight() - delta - 1);
            g2.dispose();
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\ButtonTabComponent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */