package com.timestored.sqldash.forms;

import com.google.common.base.Preconditions;
import com.timestored.sqldash.model.*;
import com.timestored.theme.Theme;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class FormWidget
        extends AbstractWidget
        implements Widget {
    final List<Widget> ws = new ArrayList<Widget>();
    private int layout = 3;
    private JPanel p;
    private JPanel b;
    private final Widget.Listener refeshListener = new Widget.Listener() {
        public void configChanged(Widget widget) {
            FormWidget.this.configChanged();
        }
    };
    private JPanel e;
    private Widget selectedWidget;

    private int counter = 1;

    public FormWidget(DesktopModel desktopModel) {
        super(desktopModel);
        this.title = "";
    }

    public FormWidget(DesktopModel desktopModel, FormWidgetDTO formWidgetDTO) {
        super(desktopModel, formWidgetDTO.id);
        this.title = formWidgetDTO.title;
        this.layout = formWidgetDTO.layout;

        for (WidgetDTO wDTO : formWidgetDTO.widgets) {
            Widget w = wDTO.newInstance(desktopModel);
            this.ws.add(w);
            w.addListener(this.refeshListener);
            this.selectedWidget = w;
        }
    }

    int getLayout() {
        return this.layout;
    }

    void setLayout(int layout) {
        if (layout == 2) {
            this.layout = 2;
        } else if (layout == 3) {
            this.layout = 3;
        }
        this.configChanged();
    }

    protected void configChanged() {
        this.refresh();
        super.configChanged();
    }

    public void tabChanged(Queryable queryable, ResultSet qTab) {
        for (Widget w : this.ws) {
            w.tabChanged(queryable, qTab);
        }
    }

    public void queryError(Queryable queryable, Exception e) {
        for (Widget w : this.ws) {
            w.queryError(queryable, e);
        }
    }

    public void argChange(Map<String, Object> changes) {
        for (Widget w : this.ws) {
            w.argChange(changes);
        }
    }

    public synchronized JPanel getPanel() {
        if (this.p == null) {
            this.p = new JPanel(new BorderLayout());
            this.p.setBorder(BorderFactory.createTitledBorder(""));
            this.b = new JPanel();
            this.p.add(new JScrollPane(this.b), "Center");
            this.refresh();
        }
        return this.p;
    }

    private synchronized void refresh() {
        if (this.b != null) {
            this.b.removeAll();
            this.b.setLayout(new BoxLayout(this.b, this.layout));
            for (Widget w : this.ws) {
                JPanel p = w.getPanel();
                this.b.add(p);
            }
            this.b.add(Box.createVerticalGlue());
            this.b.revalidate();
            this.b.repaint();
        }
    }

    List<Widget> getWidgets() {
        return this.ws;
    }

    void addWidget(Widget w) {
        if (w.getTitle().trim().isEmpty()) {
            w.setTitle("Label " + this.counter);
        }
        if (w instanceof ListSelectionWidget) {
            ListSelectionWidget lsw = (ListSelectionWidget) w;
            if (lsw.getArgKey().trim().isEmpty()) {
                lsw.setArgKey("key" + this.counter);
            }
        }
        this.counter++;
        this.ws.add(w);
        w.addListener(this.refeshListener);
        this.selectedWidget = w;
        this.configChanged();
    }

    void removeWidget(Widget w) {
        int idx = this.ws.indexOf(w);
        this.ws.remove(w);
        if (this.ws.isEmpty()) {
            this.selectedWidget = null;
        } else {
            if (idx > this.ws.size() - 1) {
                idx--;
            }
            this.selectedWidget = this.ws.get(idx);
        }
        w.removeListener(this.refeshListener);
        this.configChanged();
    }

    void moveWidgetUp(Widget w) {
        this.moveWidget(w, -1);
    }

    void moveWidgetDown(Widget w) {
        this.moveWidget(w, 1);
    }

    private void moveWidget(Widget w, int direction) {
        if (direction != 1 && direction != -1) {
            throw new IllegalArgumentException("moveWidget - small steps only");
        }
        int curI = this.ws.indexOf(w);
        int destI = curI + direction;
        if (curI >= 0 && destI >= 0 && destI < this.ws.size()) {
            Widget temp = this.ws.get(destI);
            this.ws.set(destI, w);
            this.ws.set(curI, temp);
            this.configChanged();
        }
    }

    public synchronized JPanel getEditorPanel() {
        if (this.e == null) {
            this.e = new FormWidgetEditorPanel(this, this.getDesktopModel());
        }
        return this.e;
    }

    public synchronized void invalidatePanelCache() {
        this.p = null;
        this.b = null;
        this.e = null;
        for (Widget w : this.ws) {
            w.invalidatePanelCache();
            w.removeListener(this.refeshListener);
        }
    }

    public Collection<Queryable> getQueryables() {
        List<Queryable> q = new ArrayList<Queryable>();
        for (Widget w : this.ws) {
            q.addAll(w.getQueryables());
        }
        return q;
    }

    public Widget getSelectedWidget() {
        return this.selectedWidget;
    }

    public void setSelectedWidget(Widget selectedWidget) {
        Preconditions.checkArgument((selectedWidget == null || this.ws.contains(selectedWidget)));
        if (selectedWidget != this.selectedWidget) {
            this.selectedWidget = selectedWidget;
            this.configChanged();
        }
    }

    public Icon getIcon() {
        return Theme.CIcon.TAB_GO.get16();
    }

    public WidgetDTO getDTO() {
        return new FormWidgetDTO(this);
    }

    static class WidgetListCellRenderer
            extends DefaultListCellRenderer {
        static final DefaultListCellRenderer INSTANCE = new WidgetListCellRenderer();
        private static final long serialVersionUID = 1L;

        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Widget) {
                Widget w = (Widget) value;
                label.setText(w.getTitle());
                label.setIcon(w.getIcon());
            }
            return label;
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\forms\FormWidget.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */