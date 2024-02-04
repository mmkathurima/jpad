package com.timestored.sqldash.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.timestored.connections.ConnectionManager;
import com.timestored.sqldash.ServerNameComboBox;
import com.timestored.swingxx.SwingUtils;
import com.timestored.theme.Theme;
import jsyntaxpane.DefaultSyntaxKit;
import org.jdesktop.swingx.combobox.ListComboBoxModel;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class QueryableEditorPanel
        extends JPanel
        implements Queryable.Listener {
    private static final long serialVersionUID = 1L;
    private final JEditorPane codeEditor;
    private final JButton helpArgsButton;
    private final RefreshRateComboBoxModel refreshRateComboBoxModel = new RefreshRateComboBoxModel();
    private final JComboBox refreshRateComboBox = new JComboBox(this.refreshRateComboBoxModel);
    private final ConnectionManager connectionManager;
    private Queryable activeApp;
    private DesktopModel desktopModel;

    public QueryableEditorPanel(ConnectionManager connectionManager) {
        this.connectionManager = Preconditions.checkNotNull(connectionManager);
        this.setLayout(new BorderLayout(0, 0));
        this.setBorder(BorderFactory.createTitledBorder("Query"));

        DefaultSyntaxKit.initKit();
        this.codeEditor = new JEditorPane();

        final String argsHelpText = "Preview what query is being sent to the server";
        this.helpArgsButton = new JButton(argsHelpText, Theme.CIcon.INFO.get16());
        this.helpArgsButton.setAlignmentX(0.0F);
        this.helpArgsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (QueryableEditorPanel.this.desktopModel != null) {
                    JDialog d = new JDialog();
                    d.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                    SwingUtils.addEscapeCloseListener(d);
                    d.add(Theme.getTextArea("translatedCode", QueryableEditorPanel.this.codeEditor.getText()));
                    d.setSize((int) (QueryableEditorPanel.this.codeEditor.getWidth() * 0.8D), (int) (QueryableEditorPanel.this.codeEditor.getHeight() * 0.6D));
                    d.setLocationRelativeTo(QueryableEditorPanel.this.helpArgsButton);
                    d.addMouseListener(new MouseAdapter() {
                        public void mouseExited(MouseEvent e) {
                            d.setVisible(false);
                            d.dispose();
                        }
                    });
                    d.setVisible(true);
                }
            }
        });

        JScrollPane scrPane = new JScrollPane(this.codeEditor);

        this.doLayout();
        this.codeEditor.setContentType("text/sql");
        this.codeEditor.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                if (ke.isControlDown() && ke.getKeyCode() == 10 &&
                        QueryableEditorPanel.this.activeApp != null) {
                    QueryableEditorPanel.this.activeApp.setQuery(QueryableEditorPanel.this.codeEditor.getText());
                }
            }
        });

        JButton btnRefresh = new JButton("<html>Set<br/>Query<html>");
        btnRefresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (QueryableEditorPanel.this.activeApp != null) {
                    QueryableEditorPanel.this.activeApp.setQuery(QueryableEditorPanel.this.codeEditor.getText());
                }
            }
        });
        btnRefresh.setPreferredSize(new Dimension(80, 60));

        this.refreshRateComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (QueryableEditorPanel.this.activeApp != null) {
                    int rateInMillis = QueryableEditorPanel.this.refreshRateComboBoxModel.getRefreshRate();
                    if (QueryableEditorPanel.this.activeApp.getRefreshPeriod() != rateInMillis) {
                        QueryableEditorPanel.this.activeApp.setRefreshPeriod(rateInMillis);
                    }
                }
            }
        });

        JPanel p = new JPanel();
        p.add(btnRefresh);

        this.add(scrPane, "Center");
        this.add(p, "East");
    }

    public void display(Queryable activeApp) {
        if (this.activeApp != activeApp) {
            if (this.activeApp != null) {
                this.activeApp.removeListener(this);
            }
            this.activeApp = activeApp;
            if (activeApp != null) {
                this.activeApp.addListener(this);
            }
        }
        Theme.InputLabeller IL = Theme.getInputLabeller();
        ServerNameComboBox cb = new ServerNameComboBox(this.connectionManager, activeApp);

        Box b = Box.createHorizontalBox();
        b.add(IL.get("Server:", cb, "serverQueryNameBox"));
        b.add(IL.get("Refresh Rate:", this.refreshRateComboBox, "refreshRateComboBox"));
        b.add(Box.createHorizontalGlue());
        b.add(Box.createHorizontalGlue());
        b.add(Box.createHorizontalGlue());
        b.add(Box.createHorizontalGlue());

        b.add(Box.createHorizontalGlue());
        this.add(b, "North");
        this.refresh();
    }

    void setDesktopModel(DesktopModel desktopModel) {
        this.desktopModel = desktopModel;
    }

    private void refresh() {
        boolean enabled = (this.activeApp != null);

        this.codeEditor.setEnabled(enabled);
        this.refreshRateComboBox.setEnabled(enabled);

        if (enabled) {
            this.codeEditor.setText(this.activeApp.getQuery());
            this.refreshRateComboBoxModel.setSelectedRefreshRate(this.activeApp.getRefreshPeriod());
        } else {
            this.codeEditor.setText("");
        }
    }

    public void configChanged(Queryable queryable) {
        this.refresh();
    }

    private static class RefreshRateComboBoxModel
            extends ListComboBoxModel<String> {
        private static final List<String> L = new ArrayList<>();
        private static final List<Integer> VALS = Lists.newArrayList(0, 100, 1000, 10000, 60000, -1);

        static {
            L.add("As Fast as Possible");
            L.add("Every 100 Milliseconds");
            L.add("Every 1   Second");
            L.add("Every 10  Seconds");
            L.add("Every Minute");
            L.add("Only on Interaction");
        }

        public RefreshRateComboBoxModel() {
            super(L);
        }

        public void setSelectedRefreshRate(int refreshPeriod) {
            int idx = VALS.indexOf(refreshPeriod);
            if (idx > -1 && idx < L.size()) {
                String s = L.get(idx);
                if (s != null) {
                    this.setSelectedItem(s);
                }
            }
        }

        public int getRefreshRate() {
            int index = L.indexOf(this.getSelectedItem());
            return VALS.get(index);
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\model\QueryableEditorPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */