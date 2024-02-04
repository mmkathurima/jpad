package com.timestored.command;

import com.timestored.swingxx.SwingUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

class CommandPanel
        extends JPanel {
    private static final Logger LOG = Logger.getLogger(CommandPanel.class.getName());

    private static final long serialVersionUID = 1L;
    private final JList list;
    private ChangeListener changeListener;
    private List<Command> docsShown = new ArrayList<>();

    public CommandPanel() {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createRaisedBevelBorder());

        this.list = new JList(new Object[]{""});
        this.list.setSelectionMode(0);
        this.list.setCellRenderer(CommandRenderer.getInstance());

        this.list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && !CommandPanel.this.list.isSelectionEmpty() &&
                        CommandPanel.this.changeListener != null) {
                    CommandPanel.this.changeListener.changedTo((Command) CommandPanel.this.list.getSelectedValue());
                }
            }
        });
    }

    void setCloseAction(Action action) {
        this.list.getActionMap().put("closeAll", action);
        this.list.getInputMap().put(SwingUtils.ESC_KEYSTROKE, "closeAll");
    }

    public void moveDown() {
        this.move(1);
    }

    public void moveUp() {
        this.move(-1);
    }

    private void move(int direction) {
        int sel = this.list.getSelectedIndex() + direction;
        int cSize = this.list.getModel().getSize();
        if (cSize > 0) {
            this.list.setSelectedIndex((sel < 0) ? 0 : ((sel >= cSize) ? (cSize - 1) : sel));
            this.list.ensureIndexIsVisible(this.list.getSelectedIndex());
        }
    }

    public void setCommands(Collection<Command> docsShown) {
        LOG.info("setDocsShown");
        this.docsShown = new ArrayList<>(docsShown);
        this.removeAll();
        this.list.setModel(new DefaultComboBoxModel(docsShown.toArray()));
        if (!docsShown.isEmpty()) {
            this.list.setSelectedIndex(0);
            this.add(new JScrollPane(this.list), "Center");
        } else {
            JLabel l = new JLabel("No matches found");
            this.add(l, "Center");
        }
        this.revalidate();
        this.repaint();
    }

    public Command getSelectedCommand() {
        Object o = this.list.getSelectedValue();
        return (o instanceof Command) ? (Command) o : null;
    }

    public void setSelectedCommand(Command command) {
        int p = this.docsShown.indexOf(command);
        if (p > -1 && p < this.list.getModel().getSize()) {
            this.list.setSelectedIndex(p);
            LOG.info("setSelectedCommand setSelectedIndex " + p);
        }
    }

    void setSelectAction(Action action) {
        KeyStroke enter = KeyStroke.getKeyStroke(10, 0);

        this.list.getInputMap().put(enter, enter);
        this.list.getActionMap().put(enter, action);

        this.list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Action action = CommandPanel.this.list.getActionMap().get(enter);

                    if (action != null) {

                        ActionEvent ae = new ActionEvent(CommandPanel.this.list, 1001, "");
                        action.actionPerformed(ae);
                    }
                }
            }
        });
    }

    void setChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\command\CommandPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */