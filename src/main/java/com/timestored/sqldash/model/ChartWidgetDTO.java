package com.timestored.sqldash.model;

import com.google.common.base.MoreObjects;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.timestored.sqldash.chart.ChartTheme;
import com.timestored.sqldash.chart.ViewStrategy;
import com.timestored.sqldash.chart.ViewStrategyFactory;


@XStreamAlias("chart")
class ChartWidgetDTO
        implements WidgetDTO {
    @XStreamAsAttribute
    private final String server;
    @XStreamAsAttribute
    private final int refresh;
    @XStreamAsAttribute
    private final String query;
    @XStreamAsAttribute
    public int id;
    @XStreamAsAttribute
    public String title;
    @XStreamAsAttribute
    public String theme;
    @XStreamAsAttribute
    public String type;

    public ChartWidgetDTO(ChartWidget widget) {
        this.title = widget.getTitle();

        Queryable q = widget.getQ();
        this.query = q.getQuery();
        this.server = q.getServerName();
        this.refresh = q.getRefreshPeriod();

        this.theme = widget.getChartTheme().getTitle();
        this.id = widget.getId();
        this.type = widget.getViewStrategy().getDescription();
    }

    public ViewStrategy getViewStrategy() {
        for (ViewStrategy vs : ViewStrategyFactory.getStrategies()) {
            if (vs.getDescription().equals(this.type)) {
                return vs;
            }
        }
        return ViewStrategyFactory.getStrategies().get(0);
    }


    public Queryable getQueryable() {
        return new Queryable(this.server, this.query, this.refresh);
    }

    public Widget newInstance(DesktopModel desktopModel) {
        return new ChartWidget(desktopModel, this);
    }

    public ChartTheme getChartTheme() {
        for (ChartTheme ct : ViewStrategyFactory.getThemes()) {
            if (ct.getTitle().equals(this.theme)) {
                return ct;
            }
        }
        return ViewStrategyFactory.getThemes().get(0);
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("title", this.title).add("query", this.query).add("server", this.server).add("theme", this.theme).add("id", this.id).add("refresh", this.refresh).add("type", this.type).toString();
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\model\ChartWidgetDTO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */