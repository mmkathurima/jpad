package com.timestored.sqldash.forms;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.timestored.sqldash.model.DesktopModel;
import com.timestored.sqldash.model.Queryable;
import com.timestored.sqldash.model.Widget;
import com.timestored.sqldash.model.WidgetDTO;

import java.util.Iterator;

@XStreamAlias("listinput")
public class ListSelectionWidgetDTO implements WidgetDTO {
    static BiMap<String, ListSelectionWidget.SELECTOR_TYPE> enumMap = HashBiMap.create();

    static {
        enumMap.put("checkbox", ListSelectionWidget.SELECTOR_TYPE.CHECKBOX);
        enumMap.put("combo", ListSelectionWidget.SELECTOR_TYPE.COMBOBOX);
        enumMap.put("jlist", ListSelectionWidget.SELECTOR_TYPE.LIST);
        enumMap.put("radio", ListSelectionWidget.SELECTOR_TYPE.RADIOBUTTON);
    }

    @XStreamAsAttribute
    final int id;
    @XStreamAsAttribute
    final String title;
    @XStreamAsAttribute
    final String argKey;
    @XStreamAsAttribute
    final String tooltip;
    @XStreamAsAttribute
    final String hardcodedOptionsCsv;
    @XStreamAsAttribute
    private final String query;
    @XStreamAsAttribute
    private final String server;
    @XStreamAsAttribute
    private final int refresh;
    @XStreamAsAttribute
    private final String selectorType;

    ListSelectionWidgetDTO(ListSelectionWidget listWidget) {
        this.id = listWidget.getId();
        this.title = listWidget.getTitle();

        Iterator<Queryable> it = listWidget.getQueryables().iterator();
        if (it.hasNext()) {
            Queryable q = it.next();
            this.query = q.getQuery();
            this.server = q.getServerName();
            this.refresh = q.getRefreshPeriod();
        } else {
            this.query = "";
            this.server = "";
            this.refresh = 0;
        }

        this.argKey = listWidget.getArgKey();
        this.tooltip = listWidget.getTooltip();
        this.hardcodedOptionsCsv = listWidget.getHardcodedOptionsCsv();

        this.selectorType = enumMap.inverse().get(listWidget.getSelectorType());
    }

    public Widget newInstance(DesktopModel desktopModel) {
        return new ListSelectionWidget(desktopModel, this);
    }

    public Queryable getQueryable() {
        return new Queryable(this.server, this.query, this.refresh);
    }

    public ListSelectionWidget.SELECTOR_TYPE getSelectorType() {
        ListSelectionWidget.SELECTOR_TYPE st = enumMap.get(this.selectorType);
        if (st == null) {
            st = ListSelectionWidget.SELECTOR_TYPE.COMBOBOX;
        }
        return st;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\forms\ListSelectionWidgetDTO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */