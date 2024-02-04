package com.timestored.sqldash.forms;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.timestored.sqldash.model.*;
import com.timestored.sqldash.theme.DBIcons;
import com.timestored.theme.Theme;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.*;

class ListSelectionWidget
        extends AbstractWidget
        implements Widget {
    private final Queryable q;
    private final Collection<Queryable> qs = new ArrayList<>(1);
    private String argKey = "";
    private String tooltip = "";
    private String hardcodedOptionsCsv;
    private boolean firstNonEmptyTabSeen;
    private List<String> hardcodedOptions = Collections.emptyList();
    private List<String> RSoptions = Collections.emptyList();
    private List<String> selections = Collections.emptyList();
    private WListSelector wListSelector;
    private ListEditorPanel ep;
    private SELECTOR_TYPE selectorType;
    private JPanel p;

    public ListSelectionWidget(DesktopModel desktopModel, SELECTOR_TYPE selectorType) {
        super(desktopModel);
        this.selectorType = selectorType;
        this.q = new Queryable();
        this.qs.add(this.q);
        this.q.addListener(new Queryable.Listener() {
            public void configChanged(Queryable queryable) {
                ListSelectionWidget.this.configChanged();
            }
        });
    }

    public ListSelectionWidget(DesktopModel desktopModel, ListSelectionWidgetDTO listSelectionWidgetDTO) {
        super(desktopModel, listSelectionWidgetDTO.id);
        this.q = listSelectionWidgetDTO.getQueryable();
        this.qs.add(this.q);
        this.title = listSelectionWidgetDTO.title;
        this.selectorType = listSelectionWidgetDTO.getSelectorType();
        this.argKey = listSelectionWidgetDTO.argKey;
        this.tooltip = listSelectionWidgetDTO.tooltip;

        this.hardcodedOptionsCsv = listSelectionWidgetDTO.hardcodedOptionsCsv;
        if (this.hardcodedOptionsCsv != null) {
            this.hardcodedOptions = Arrays.asList(this.hardcodedOptionsCsv.trim().split(","));
        }

        this.q.addListener(new Queryable.Listener() {
            public void configChanged(Queryable queryable) {
                ListSelectionWidget.this.configChanged();
            }
        });

        Object val = desktopModel.getArg(this.argKey);
        if (val == null && this.hardcodedOptions != null && this.hardcodedOptions.size() > 0) {
            if (this.selectorType.allowsMultipleChoices()) {
                this.selections = this.hardcodedOptions;
                desktopModel.setArg(this.argKey, this.hardcodedOptions);
            } else {
                this.selections = Collections.singletonList(this.hardcodedOptions.get(0));
                desktopModel.setArg(this.argKey, this.hardcodedOptions.get(0));
            }
        }
    }

    public JPanel getPanel() {
        synchronized (this) {
            if (this.wListSelector == null) {
                this.p = new JPanel(new BorderLayout());
                this.wListSelector = this.selectorType.get(this, this.getDesktopModel());
                this.p.add(this.wListSelector.getDisplay(), "Center");
                this.wListSelector.refresh();
            }
        }
        return this.p;
    }

    protected void configChanged() {
        if (this.q.getQuery().isEmpty()) {
            this.RSoptions = Collections.emptyList();
        }
        if (this.wListSelector != null) {

            this.wListSelector.refresh();
        }
        super.configChanged();
    }

    public JPanel getEditorPanel() {
        synchronized (this) {
            if (this.ep == null) {
                this.ep = new ListEditorPanel(this);
            }
        }
        return this.ep;
    }

    public void invalidatePanelCache() {
        synchronized (this) {
            this.wListSelector = null;
            if (this.ep != null) {
                this.ep.removeMyListeners();
            }
            this.ep = null;
        }
    }

    public void argChange(Map<String, Object> changes) {
        if (changes.containsKey(this.argKey)) {
            Object value = changes.get(this.argKey);
            List<String> r = new ArrayList<>();
            if (value instanceof List) {
                for (String o : (List<String>) value) {
                    r.add("" + o);
                }
            } else if (value instanceof String) {
                r.add((String) value);
            }
            this.selections = r;
            if (this.wListSelector != null) {
                this.wListSelector.refresh();
            }
        }
    }

    public Collection<Queryable> getQueryables() {
        return (this.q.getQuery().trim().length() > 0) ? this.qs : Collections.emptyList();
    }

    public void queryError(Queryable queryable, Exception e) {
        if (queryable == this.q) {
            this.RSoptions = List.of("Query Error");
            if (this.wListSelector != null) {
                this.wListSelector.refresh();
            }
        }
    }

    public void tabChanged(Queryable queryable, ResultSet rs) {
        try {
            int colCount = rs.getMetaData().getColumnCount();
            if (queryable == this.q && colCount > 0) {
                if (this.title == null) {
                    this.setTitle(rs.getMetaData().getColumnLabel(1));
                }
                List<String> v = new ArrayList<>();
                rs.beforeFirst();
                while (rs.next()) {
                    v.add("" + rs.getObject(1));
                }

                this.RSoptions = v;

                if (!this.firstNonEmptyTabSeen && v.size() > 0) {
                    Object val = this.desktopModel.getArg(this.argKey);
                    if (val == null && this.hardcodedOptions.isEmpty()) {
                        if (this.selectorType.allowsMultipleChoices()) {
                            this.desktopModel.setArg(this.argKey, v);
                        } else {
                            this.desktopModel.setArg(this.argKey, v.get(0));
                        }
                    }
                    this.firstNonEmptyTabSeen = true;
                }

                if (this.wListSelector != null) {
                    this.wListSelector.refresh();
                }
            }
        } catch (SQLException e) {
        }
    }

    public String getTitle() {
        return (this.title == null) ? "label" : this.title;
    }

    String getArgKey() {
        return this.argKey;
    }

    void setArgKey(String argKey) {
        this.argKey = argKey;
        this.configChanged();
    }

    String getTooltip() {
        return this.tooltip;
    }

    void setTooltip(String tooltip) {
        this.tooltip = tooltip;
        this.configChanged();
    }

    String getHardcodedOptionsCsv() {
        return this.hardcodedOptionsCsv;
    }

    void setHardcodedOptions(String hardcodedOptionsCsv) {
        String h = Preconditions.checkNotNull(hardcodedOptionsCsv).trim();
        if (!hardcodedOptionsCsv.equals(this.hardcodedOptionsCsv)) {
            if (h.length() > 0) {
                this.hardcodedOptions = Arrays.asList(h.split(","));
            } else {
                this.hardcodedOptions = Collections.emptyList();
            }
            this.hardcodedOptionsCsv = hardcodedOptionsCsv;
            this.configChanged();
        }
    }

    public SELECTOR_TYPE getSelectorType() {
        return this.selectorType;
    }

    void setSelectorType(SELECTOR_TYPE selectorType) {
        if (!selectorType.equals(this.selectorType)) {

            this.selectorType = selectorType;
            this.wListSelector = selectorType.get(this, this.getDesktopModel());
            synchronized (this) {
                if (this.p != null) {
                    this.p.removeAll();
                    this.p.add(this.wListSelector.getDisplay(), "Center");
                    this.p.invalidate();
                    this.p.repaint();
                }
            }
            this.configChanged();
        }
    }

    List<String> getOptions() {
        if (this.RSoptions.size() == 0) {
            return this.hardcodedOptions;
        }
        if (this.hardcodedOptions.size() == 0) {
            return this.RSoptions;
        }
        List<String> r = new ArrayList<>(this.hardcodedOptions);
        r.addAll(this.RSoptions);
        return r;
    }

    List<String> getSelections() {
        return this.selections;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("super", super.toString()).add("selectorType", this.selectorType).toString();
    }

    public Icon getIcon() {
        return this.selectorType.imageIcon;
    }

    public WidgetDTO getDTO() {
        return new ListSelectionWidgetDTO(this);
    }

    public enum SELECTOR_TYPE {
        COMBOBOX(DBIcons.COMBOBOX.get16(), false) {
            public WListSelector get(ListSelectionWidget lsw, DesktopModel desktopModel) {
                return new WComboBoxWListSelector(lsw, desktopModel);
            }
        },
        RADIOBUTTON(DBIcons.RADIOBUTTON.get16(), false) {
            public WListSelector get(ListSelectionWidget lsw, DesktopModel desktopModel) {
                return new WRadioButtonsWListSelector(lsw, desktopModel);
            }
        },
        CHECKBOX(DBIcons.CHECKBOX.get16(), true) {
            public WListSelector get(ListSelectionWidget lsw, DesktopModel desktopModel) {
                return new WCheckboxWListSelector(lsw, desktopModel);
            }
        },
        LIST(DBIcons.LIST.get16(), true) {
            public WListSelector get(ListSelectionWidget lsw, DesktopModel desktopModel) {
                return new WListWListSelector(lsw, desktopModel);
            }
        };

        private final ImageIcon imageIcon;
        private final boolean multipleChoice;

        SELECTOR_TYPE(ImageIcon imageIcon, boolean multipleChoice) {
            this.imageIcon = imageIcon;
            this.multipleChoice = multipleChoice;
        }

        public boolean allowsMultipleChoices() {
            return this.multipleChoice;
        }

        public ImageIcon getImageIcon() {
            return this.imageIcon;
        }

        public abstract WListSelector get(ListSelectionWidget param1ListSelectionWidget, DesktopModel param1DesktopModel);
    }

    private static class ListEditorPanel
            extends JPanel {
        private static final long serialVersionUID = 1L;
        private final QueryableEditorPanel appQueryEditorPanel;

        public ListEditorPanel(ListSelectionWidget listWidget) {
            this.appQueryEditorPanel = new QueryableEditorPanel(listWidget.desktopModel.getConnectionManager());
            this.appQueryEditorPanel.display(listWidget.q);

            JComboBox<ListSelectionWidget.SELECTOR_TYPE> typeCB = new JComboBox<>(ListSelectionWidget.SELECTOR_TYPE.values());
            typeCB.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    listWidget.setSelectorType((ListSelectionWidget.SELECTOR_TYPE) typeCB.getSelectedItem());
                }
            });
            typeCB.setSelectedItem(listWidget.selectorType);

            JTextField keyTF = new JTextField(20);
            keyTF.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    listWidget.setArgKey(keyTF.getText());
                }
            });
            keyTF.setText(listWidget.getArgKey());

            JTextField optionsTF = new JTextField(40);
            optionsTF.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    listWidget.setHardcodedOptions(optionsTF.getText());
                }
            });
            optionsTF.setText(listWidget.getHardcodedOptionsCsv());

            JTextField labelTF = new JTextField(20);
            labelTF.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    listWidget.setTitle(labelTF.getText());
                }
            });
            labelTF.setText(listWidget.getTitle());

            NumberFormat intNF = NumberFormat.getIntegerInstance();
            intNF.setMaximumFractionDigits(0);

            Box cbp = Box.createVerticalBox();

            this.setLayout(new BorderLayout());
            Theme.InputLabeller il = Theme.getInputLabeller(50, 20);

            cbp.add(il.get("GUI Type:", typeCB, "typeCB", "Specify the appearance of this selection."));
            cbp.add(il.get("Key:", keyTF, "keyTF", "Specify a parameter key that this component sets."));
            cbp.add(il.get("Options:", optionsTF, "optionsTF", "Specify hardcopied options."));
            cbp.add(il.get("Label:", labelTF, "labelTF", "Specify an input label."));
            cbp.setBorder(BorderFactory.createEtchedBorder());
            cbp.add(Box.createVerticalGlue());

            this.add(new JScrollPane(cbp), "West");
            this.add(this.appQueryEditorPanel, "Center");
        }

        public void removeMyListeners() {
            this.appQueryEditorPanel.display(null);
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\forms\ListSelectionWidget.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */