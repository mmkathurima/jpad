package com.timestored.sqldash.model;

import com.google.common.base.MoreObjects;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("desktop")
class DesktopDTO {
    @XStreamAsAttribute
    public final String title;
    public List<WorkspaceDTO> workspaces = new ArrayList<>();

    public DesktopDTO(DesktopModel desktopModel) {
        this.title = desktopModel.getTitle();
        for (WorkspaceModel ws : desktopModel.getWorkspaces()) {
            this.workspaces.add(new WorkspaceDTO(ws));
        }
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("title", this.title).add("workspaces", this.workspaces).toString();
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\model\DesktopDTO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */