package com.timestored.theme;

import com.timestored.misc.HtmlUtils;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;

import javax.swing.Icon;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class Theme {
    public static final Color SUB_HEADER_FG_COLOR = new Color(242, 242, 242);
    public static final Color SUB_HEADER_BG_COLOR = new Color(174, 185, 210);
    public static final int GAP = 4;
    public static final Border CENTRE_BORDER = BorderFactory.createEmptyBorder(4, 4, 4, 4);

    private static final Font CODE_FONT = new Font("Monospaced", Font.PLAIN, 14);
    private static final Color HEADER_FG_COLOR = new Color(252, 252, 252);
    private static final Color HEADER_BG_COLOR = new Color(154, 165, 190);
    private static final Border OFFSET_BORDER = BorderFactory.createEmptyBorder(0, 0, 4, 4);
    private static final ListCellRenderer LIST_RENDERER = new ActionListCellRenderer();

    private static final InputLabeller INPUT_LABELLER_INSTANCE = new InputLabeller();

    private static Font getHeaderFont(Font curFont) {
        return new Font(curFont.getName(), Font.BOLD, curFont.getSize() + 7);
    }

    private static Font getSubHeaderFont(Font curFont) {
        return new Font(curFont.getName(), Font.BOLD, curFont.getSize() + 3);
    }

    public static Font getCodeFont() {
        return CODE_FONT;
    }

    public static JPanel getHeader(String title) {
        JPanel outPanel = new JPanel(new BorderLayout());
        outPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(getHeaderFont(titleLabel.getFont()));
        titleLabel.setForeground(HEADER_FG_COLOR);
        titleLabel.setBorder(CENTRE_BORDER);
        headerPanel.add(titleLabel, "Center");
        headerPanel.setBackground(HEADER_BG_COLOR);
        outPanel.add(headerPanel, "North");
        return outPanel;
    }

    public static JPanel getPlainReadonlyTable(TableModel tableModel) {
        JXTable table = new JXTable(tableModel);
        table.getTableHeader().setReorderingAllowed(false);
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(table.getTableHeader(), "North");
        tablePanel.add(table, "Center");
        tablePanel.setAlignmentX(0.0F);
        tablePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JPanel tContainerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tContainerPanel.add(tablePanel);
        table.packAll();
        return tContainerPanel;
    }

    public static JXTable getStripedTable(TableModel tableModel) {
        JXTable table = new JXTable(tableModel);
        JTableHeader anHeader = table.getTableHeader();
        anHeader.setForeground(Color.BLACK);
        anHeader.setBackground(Color.GRAY);
        table.setHighlighters(HighlighterFactory.createSimpleStriping());
        return table;
    }

    public static JPanel getSubHeader(String title) {
        return getSubHeader(title, SUB_HEADER_FG_COLOR, SUB_HEADER_BG_COLOR);
    }

    public static JPanel getHorizontalRule() {
        JPanel outPanel = new JPanel(new BorderLayout());
        outPanel.setBorder(BorderFactory.createEmptyBorder(1, 0, 1, 0));
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(new JLabel(" "));
        headerPanel.setBackground(SUB_HEADER_BG_COLOR);
        outPanel.add(headerPanel, "North");
        return outPanel;
    }

    public static JPanel getSubHeader(String title, Color foregroundColor, Color backgroundColor) {
        JPanel outPanel = new JPanel(new BorderLayout());
        outPanel.setBorder(BorderFactory.createEmptyBorder(4, 0, 6, 0));
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(getSubHeaderFont(titleLabel.getFont()));
        titleLabel.setForeground(foregroundColor);
        titleLabel.setBorder(CENTRE_BORDER);
        headerPanel.add(titleLabel, "Center");
        headerPanel.setBackground(backgroundColor);
        outPanel.add(headerPanel, "North");
        return outPanel;
    }

    public static int getGap() {
        return 4;
    }

    public static Border getCentreBorder() {
        return CENTRE_BORDER;
    }

    public static Border getOffsetBorder() {
        return OFFSET_BORDER;
    }

    public static JPanel getVerticalBoxPanel() {
        return new BoxyPanel();
    }

    public static JPanel wrap(String title, Component centerPanel, String text) {
        JTextArea description = null;
        if (text != null && text.length() > 0) {
            description = new JTextArea(text);
            description.setLineWrap(true);
        }
        return wrap(title, centerPanel, description);
    }

    public static JPanel wrap(String title, Component centerPanel) {
        return wrap(title, centerPanel, (Component) null);
    }

    public static JPanel wrap(String title, Component centerPanel, Component description) {
        JPanel containerPanel = new JPanel(new BorderLayout());
        JPanel headerPanel = getSubHeader(title);
        containerPanel.add(headerPanel, "North");
        if (centerPanel != null) {
            containerPanel.add(centerPanel, "Center");
        }

        if (description != null) {
            containerPanel.add(description, "South");
        }
        return containerPanel;
    }

    public static JEditorPane getHtmlText(String html) {
        JEditorPane ep = new JEditorPane("text/html", html);

        ep.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED))
                    HtmlUtils.browse(e.getURL().toString());
            }
        });
        ep.setEditable(false);
        ep.setBackground((new JLabel()).getBackground());
        return ep;
    }

    public static JTextArea getTextArea(String name, String txt) {
        JTextArea ta = new JTextArea(txt);
        ta.setWrapStyleWord(true);
        ta.setLineWrap(true);
        ta.setOpaque(false);
        ta.setEditable(false);
        return ta;
    }

    public static InputLabeller getInputLabeller(int labelWidth, int labelHeight) {
        return new InputLabeller(labelWidth, labelHeight);
    }

    public static InputLabeller getInputLabeller() {
        return INPUT_LABELLER_INSTANCE;
    }

    public static Box getFormRow(JComponent c, String label, String tooltip) {
        return getFormRow(c, label, tooltip, null);
    }

    public static Box getFormRow(JComponent c, String label, String tooltip, JComponent rowEnd) {
        JLabel l = new JLabel(label);
        if (tooltip != null) {
            c.setToolTipText(tooltip);
            l.setToolTipText(tooltip);
        }
        l.setLabelFor(c);

        Box b = Box.createHorizontalBox();
        b.add(l);
        b.add(c);
        if (rowEnd != null) {
            b.add(rowEnd);
        }
        return b;
    }

    public static ListCellRenderer getActionListCellRenderer() {
        return LIST_RENDERER;
    }

    public static Box getErrorBox(String header, Component... components) {
        Box box = Box.createVerticalBox();
        box.add(getSubHeader(header, Color.RED, Color.PINK));
        for (Component c : components) {
            box.add(c);
        }
        return box;
    }

    public static JButton makeButton(String text, ActionListener actionListener) {
        JButton btn = new JButton(text);
        btn.setBorder(new EmptyBorder(6, 15, 6, 15));
        btn.setName(text + "Button");
        btn.addActionListener(actionListener);
        return btn;
    }

    public static String getTextFromDialog(Component parent, String title, String defaultText, String helpMsg) {
        JPanel p = new JPanel(new BorderLayout());
        JTextArea textArea = new JTextArea(defaultText);
        textArea.selectAll();
        JScrollPane scrPane = new JScrollPane(textArea);
        scrPane.setVerticalScrollBarPolicy(22);

        p.add(getHtmlText(helpMsg), "North");

        p.add(scrPane, "Center");
        String[] options = {"ok", "cancel"};
        JOptionPane optPane = new JOptionPane(p, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, null, options, options[0]);

        JDialog d = optPane.createDialog(parent, title);
        d.setSize(new Dimension(560, 400));
        d.setPreferredSize(new Dimension(560, 400));
        d.pack();

        d.addWindowListener(new WindowAdapter() {
            public void windowActivated(WindowEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        textArea.requestFocus();
                        textArea.requestFocusInWindow();
                    }
                });
            }
        });
        d.setVisible(true);

        String txt = null;
        if (options[0].equals(optPane.getValue())) {
            txt = textArea.getText();
        }
        return txt;
    }

    public enum CIcon implements com.timestored.theme.Icon {
        SERVER_ADD("server_add.png"),
        SERVER_CHART("server_chart.png"),
        SERVER_CONNECT("server_connect.png"),
        SERVER_DELETE("server_delete.png"),
        SERVER_EDIT("server_edit.png"),
        SERVER_ERROR("server_error.png"),
        SERVER_GO("server_go.png"),
        SERVER_KEY("server_key.png"),
        SERVER_LIGHTNING("server_lightning.png"),
        SERVER("server.png"),
        DELETE("delete.png"),
        SAVE("disk.png"),
        ADD_SERVER("red-dot.png"),
        TREE_ELEMENT("red-dot.png"),
        TABLE_ELEMENT("tbl.gif"),
        VIEW_ELEMENT("view.png"),
        DATE_ELEMENT("typdate.png"),
        CHAR_ELEMENT("typstring.gif"),
        NUMBER_ELEMENT("var.png"),
        LAMBDA_ELEMENT("typlambda.png"),
        FUNCTION_ELEMENT("function.png"),
        JPAD("jpad.png"),
        INFO("info.png"),
        INFO32("info32.png"),
        SQLDASH_LOGO("logo.png"),
        EYE("eye.png"),
        QSTUDIO_LOGO("qstudio.png"),
        DOCUMENT_NEW("document-new.png"),
        DOCUMENT_OPEN("document-open.png"),
        DOCUMENT_SAVE("document-save.png"),
        DOCUMENT_SAVE_AS("document-save-as.png"),
        TABLE_ADD("table_add.png"),
        TABLE_DELETE("table_delete.png"),
        CHART_CURVE("chart_curve.png"),
        TAB_GO("tab_go.png"),
        PAGE("page.png"),
        TABLE_MULTIPLE("table_multiple.png"),
        PAGE_EDIT("page_edit.png"),
        PAGE_CODE("page_code.png"),
        PAGE_RED("page_red.png"),
        PAGE_WHITE_ZIP("page_white_zip.png"),
        TABLE_ROW_DELETE("table_row_delete.png"),
        ARROW_REFRESH("arrow_refresh.png"),
        CSV("csv.png"),
        EDIT_CUT("edit-cut.png"),
        EDIT_COPY("edit-copy.png"),
        EDIT_PASTE("edit-paste.png"),
        EDIT_COMMENT("comment.png"),
        EDIT_FIND("find.png"),
        EDIT_FIND_NEXT("find-next.png"),
        EDIT_GOTO_LINE("goto-line.png"),
        EDIT_UNDO("edit-undo.png"),
        EDIT_REDO("edit-redo.png"),
        TEXT_HTML("text-html.png"),
        COPY("copy.png"),
        FUNC_COL("fncol.png"),
        RENAME("rename.png"),
        ADD("add.png"),
        ACCEPT("accept.png"),
        CANCEL("cancel.png"),
        TERMINAL("utilities-terminal.png"),
        INFORMATION("dialog-information.png"),
        WARNING("dialog-warning.png"),
        ERROR("dialog-error.png"),
        SCRIPT_GO("script-go.png"),
        CLOCK_GO("clock-go.png"),
        FOLDER_ADD("folder-add.png"),
        FOLDER_DELETE("folder-delete.png"),
        LAYOUT_DELETE("layout-delete.png"),
        LAYOUT_ADD("layout-add.png"),
        LAYOUT_EDIT("layout-edit.png"),
        DAS_FILE("das-file.png"),
        CHART_CURVE_DELETE("chart-curve-delete.png"),
        CHART_CURVE_ADD("chart-curve-add.png"),
        UP_CLOUD("upcloud.png"),
        POPUP_WINDOW("application-double.png"),
        FOLDER("folder.png");
        private final ImageIcon imageIcon;
        private final ImageIcon imageIcon16;
        private final ImageIcon imageIcon32;

        CIcon(String loc) {
            ImageIcon[] icons = IconHelper.getDiffSizesOfIcon(this.getClass().getClassLoader().getResource(loc));
            this.imageIcon = icons[0];
            this.imageIcon16 = icons[1];
            this.imageIcon32 = icons[2];
        }

        public ImageIcon get() {
            return this.imageIcon;
        }

        public BufferedImage getBufferedImage() {
            return IconHelper.getBufferedImage(this.imageIcon);
        }

        public ImageIcon get16() {
            return this.imageIcon16;
        }

        public ImageIcon get32() {
            return this.imageIcon32;
        }
    }

    public static class BoxyPanel
            extends JPanel {
        public BoxyPanel() {
            this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        }

        public Component add(Component c) {
            super.add(c).setMaximumSize(new Dimension(2147483647, (int) c.getPreferredSize().getHeight()));

            super.add(Box.createVerticalStrut(16));
            return c;
        }
    }

    public static class InputLabeller {
        private final Dimension labelDimension;

        private InputLabeller(int labelWidth, int labelHeight) {
            this.labelDimension = new Dimension(labelWidth, labelHeight);
        }

        private InputLabeller() {
            this.labelDimension = null;
        }

        public JPanel get(String labelText, Component inputComp, String inputName) {
            return this.get(labelText, inputComp, inputName, null, null);
        }

        public JPanel get(String labelText, Component inputComp, String inputName, Component helpComponent, String lblTooltip) {
            JPanel p = new JPanel();
            FlowLayout flowLayout = (FlowLayout) p.getLayout();
            flowLayout.setAlignment(FlowLayout.LEFT);

            JLabel label = new JLabel(labelText);
            if (lblTooltip != null) {
                label.setToolTipText(lblTooltip);
            }
            if (this.labelDimension != null) {
                label.setPreferredSize(this.labelDimension);
            }
            label.setLabelFor(inputComp);
            inputComp.setName(inputName);

            p.add(label);
            p.add(inputComp);
            if (helpComponent != null) {
                p.add(helpComponent);
            }

            return p;
        }

        public JPanel get(String labelText, Component inputComp, String inputName, Component helpComponent) {
            return this.get(labelText, inputComp, inputName, helpComponent, null);
        }

        public JPanel get(String labelText, Component inputComp, String inputName, String toolltip) {
            return this.get(labelText, inputComp, inputName, null, toolltip);
        }
    }

    private static class ActionListCellRenderer
            extends DefaultListCellRenderer {
        private static final long serialVersionUID = 1L;

        private ActionListCellRenderer() {
        }

        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Action) {
                Action a = (Action) value;
                label.setText((String) a.getValue("Name"));
                label.setIcon((Icon) a.getValue("SmallIcon"));
                return label;
            }
            return label;
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\theme\Theme.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */