package com.timestored.sqldash.model;

import com.google.common.base.MoreObjects;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.util.ArrayList;
import java.util.List;


@XStreamAlias("workspace")
class WorkspaceDTO {
    @XStreamAsAttribute
    public final String title;
    public final List<WidgetDTO> widgets = new ArrayList<WidgetDTO>();
    public final String jlayout;

    public WorkspaceDTO(WorkspaceModel workspaceModel) {
        this.title = workspaceModel.getTitle();
        this.jlayout = workspaceModel.getJavaLayoutXml();
        for (Widget w : workspaceModel.getApps()) {
            this.widgets.add(w.getDTO());
        }
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("title", this.title).add("widgets", this.widgets).toString();
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\model\WorkspaceDTO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */