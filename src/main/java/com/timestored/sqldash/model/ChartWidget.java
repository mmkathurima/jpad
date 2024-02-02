package com.timestored.sqldash.model;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.timestored.sqldash.chart.*;
import com.timestored.theme.Icon;
import com.timestored.theme.Theme;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


public class ChartWidget
        extends AbstractWidget {
    private final ChartViewConfiguration chartViewConfig;
    private final List<Queryable> queryable = new ArrayList<Queryable>(1);
    private final Queryable.Listener queryableListener = new Queryable.Listener() {
        public void configChanged(Queryable queryable) {
            ChartWidget.this.configChanged();
        }
    };
    private ChartTheme chartTheme;
    private ViewStrategy viewStrategy;
    private JdbcChartPanel chartPanel = null;
    private ResultSet prevRS = null;
    private Queryable q = new Queryable();
    private ViewStrategy prevNonTabVS;
    private boolean ignoreConfigChanges = false;
    private Widget.Listener updateListener;


    public ChartWidget() {
        this((DesktopModel) null);
    }


    public ChartWidget(ChartWidget app) {
        this(null, app);
    }

    public ChartWidget(DesktopModel desktopModel) {
        super(desktopModel);
        this.queryable.add(this.q);
        this.chartViewConfig = new ChartViewConfiguration();
        this.viewStrategy = ViewStrategyFactory.getStrategies().get(0);
        this.chartTheme = ViewStrategyFactory.getThemes().get(0);
        this.q.addListener(this.queryableListener);
    }

    ChartWidget(DesktopModel desktopModel, ChartWidgetDTO chartWidgetDTO) {
        super(desktopModel, chartWidgetDTO.id);
        this.q = chartWidgetDTO.getQueryable();
        this.queryable.add(this.q);
        this.title = chartWidgetDTO.title;
        this.chartViewConfig = new ChartViewConfiguration();
        this.viewStrategy = chartWidgetDTO.getViewStrategy();
        this.chartTheme = chartWidgetDTO.getChartTheme();
        this.q.addListener(this.queryableListener);
    }


    public ChartWidget(DesktopModel desktopModel, ChartWidget app) {
        super(desktopModel, app);
        this.q = new Queryable(app.getQ());
        this.queryable.add(this.q);
        this.viewStrategy = app.viewStrategy;
        this.chartTheme = app.chartTheme;
        this.chartViewConfig = null;
        this.q.addListener(this.queryableListener);
    }

    public WidgetDTO getDTO() {
        return new ChartWidgetDTO(this);
    }

    public ChartTheme getChartTheme() {
        return this.chartTheme;
    }

    public void setChartTheme(ChartTheme chartTheme) {
        this.chartTheme = chartTheme;
        configChanged();
    }

    public ViewStrategy getViewStrategy() {
        return this.viewStrategy;
    }

    public void setViewStrategy(ViewStrategy viewStrategy) {
        if (!this.viewStrategy.equals(DataTableViewStrategy.getInstance(true))) {
            this.prevNonTabVS = this.viewStrategy;
        }
        this.viewStrategy = viewStrategy;
        configChanged();
    }

    public ChartViewConfiguration getChartViewConfig() {
        return this.chartViewConfig;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("viewStrategy", this.viewStrategy).add("chartTheme", this.chartTheme).add("chartViewConfig", this.chartViewConfig).add("queryable", this.q).toString();
    }


    private JdbcChartPanel generateChart() {
        synchronized (this) {
            this.chartPanel = ViewStrategyFactory.getJdbcChartpanel();
            this.chartPanel.setViewStrategy(getViewStrategy());
            this.chartPanel.setTheme(getChartTheme());

            this.updateListener = new Widget.Listener() {
                public void configChanged(Widget app) {
                    ChartWidget.this.chartPanel.setViewStrategy(ChartWidget.this.viewStrategy);
                    ChartWidget.this.chartPanel.setTheme(ChartWidget.this.chartTheme);
                    if (!ChartWidget.this.ignoreConfigChanges) {
                        ChartWidget.this.chartPanel.update(ChartWidget.this.prevRS);
                    }
                }
            };
            addListener(this.updateListener);
        }


        return this.chartPanel;
    }

    public void invalidatePanelCache() {
        if (this.chartPanel != null) {
            synchronized (this) {
                if (this.chartPanel != null) {
                    removeListener(this.updateListener);
                    this.chartPanel = null;
                    this.updateListener = null;
                }
            }
        }
    }


    public void tabChanged(Queryable w, ResultSet rs) {
        if (this.chartPanel != null && w == this.q && !this.ignoreConfigChanges) {
            this.prevRS = rs;
            this.chartPanel.update(rs);
        }
    }

    public void queryError(Queryable w, Exception e) {
        if (this.chartPanel != null && w == this.q && !this.ignoreConfigChanges) {
            this.prevRS = null;
            this.chartPanel.update(e);
        }
    }


    public void setIgnoreConfigChanges(boolean ignoreConfigChanges) {
        this.ignoreConfigChanges = ignoreConfigChanges;
    }

    public JPanel getPanel() {
        if (this.chartPanel == null) {
            synchronized (this) {
                if (this.chartPanel == null) {
                    this.chartPanel = generateChart();
                }
            }
        }

        return this.chartPanel;
    }

    public JPanel getEditorPanel() {
        ChartEditorPanel cep = new ChartEditorPanel(this.desktopModel.getConnectionManager());
        cep.display(this);
        return cep;
    }

    public Collection<Queryable> getQueryables() {
        return this.queryable;
    }

    public Queryable getQ() {
        return this.q;
    }


    public void setQueryable(Queryable q) {
        this.q.removeListener(this.queryableListener);
        this.q = q;
        this.queryable.set(0, q);
        this.q.addListener(this.queryableListener);
        configChanged();
    }


    public void argChange(Map<String, Object> changes) {
    }


    public Icon getTSIcon() {
        if (this.viewStrategy != null && this.viewStrategy.getIcon() != null) {
            return this.viewStrategy.getIcon();
        }
        return Theme.CIcon.CHART_CURVE;
    }

    public javax.swing.Icon getIcon() {
        return getTSIcon().get16();
    }

    public Collection<Action> getActions() {
        Action toggleTabView = new AbstractAction("Toggle Table/Chart View", Theme.CIcon.TABLE_ELEMENT.get16()) {
            public void actionPerformed(ActionEvent e) {
                boolean isTab = ChartWidget.this.viewStrategy.equals(DataTableViewStrategy.getInstance(true));
                if (isTab && ChartWidget.this.prevNonTabVS != null) {
                    ChartWidget.this.setViewStrategy(ChartWidget.this.prevNonTabVS);
                } else {
                    ChartWidget.this.setViewStrategy(DataTableViewStrategy.getInstance(true));
                }
            }
        };
        return Lists.newArrayList(toggleTabView);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\model\ChartWidget.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */