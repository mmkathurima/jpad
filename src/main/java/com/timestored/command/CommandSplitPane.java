package com.timestored.command;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.ScrollableSizeHint;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;


public class CommandSplitPane
        extends JSplitPane {
    private static final long serialVersionUID = 1L;
    private final DetailsPanel detailsPanel;
    private final CommandPanel commandPanel;
    private Color fgColor;
    private Color bgColor;

    public CommandSplitPane() {
        super(1, true);

        this.commandPanel = new CommandPanel();
        this.detailsPanel = new DetailsPanel();

        JScrollPane selectionScroll = new JScrollPane(this.commandPanel);
        selectionScroll.setMinimumSize(new Dimension(75, 50));


        setLeftComponent(selectionScroll);
        setRightComponent(this.detailsPanel);
        setResizeWeight(0.33D);


        this.commandPanel.setChangeListener(new ChangeListener() {
            public void changedTo(Command command) {
                CommandSplitPane.this.detailsPanel.displayDoc(command);
            }
        });
    }

    public void moveDown() {
        this.commandPanel.moveDown();
    }

    public void moveUp() {
        this.commandPanel.moveUp();
    }

    public void setDocsShown(List<Command> docsShown) {
        this.commandPanel.setCommands(docsShown);
        if (docsShown.size() > 0)
            this.detailsPanel.displayDoc(docsShown.get(0));
    }

    public Command getSelectedCommand() {
        return this.commandPanel.getSelectedCommand();
    }

    public void setSelectAction(AbstractAction action) {
        this.commandPanel.setSelectAction(action);
    }

    public void setCloseAction(AbstractAction action) {
        this.commandPanel.setCloseAction(action);
    }

    private JPanel getCommandPanel(Command command) {
        JXPanel p = new JXPanel(new BorderLayout());
        JTextPane txtPane = new JTextPane();
        txtPane.setContentType("text/html");
        txtPane.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED))
                    Utils.browse(e.getURL().toString());
            }
        });
        JPanel header = Utils.getSubHeader(command.getTitle(), this.fgColor, this.bgColor);
        p.add(header, "North");
        txtPane.setText(command.getDetailHtml());

        txtPane.setEditable(false);

        p.setBorder(BorderFactory.createRaisedBevelBorder());
        p.setScrollableTracksViewportWidth(true);
        p.setScrollableWidthHint(ScrollableSizeHint.FIT);

        JScrollPane scrollPane = new JScrollPane(txtPane, 22, 31);


        txtPane.setCaretPosition(0);

        p.add(scrollPane, "Center");
        return p;
    }

    public void setFgColor(Color fgColor) {
        this.fgColor = fgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    private class DetailsPanel
            extends JPanel {
        private static final long serialVersionUID = 1L;


        public DetailsPanel() {
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createRaisedBevelBorder());
            setMinimumSize(new Dimension(100, 50));
        }

        void displayDoc(Command c) {
            removeAll();
            if (c != null) {
                add(CommandSplitPane.this.getCommandPanel(c), "Center");
            }
            revalidate();
            repaint();
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\command\CommandSplitPane.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */