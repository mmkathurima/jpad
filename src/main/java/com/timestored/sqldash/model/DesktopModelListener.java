package com.timestored.sqldash.model;

public interface DesktopModelListener extends DesktopModelArgListener {
    void workspaceSelected(WorkspaceModel paramWorkspaceModel);

    void workspaceTitleChanged(WorkspaceModel paramWorkspaceModel);

    void appRemoved(WorkspaceModel paramWorkspaceModel, Widget paramWidget);

    void appAdded(WorkspaceModel paramWorkspaceModel, Widget paramWidget);

    void appSelected(WorkspaceModel paramWorkspaceModel, Widget paramWidget);

    void appEdited(WorkspaceModel paramWorkspaceModel, Widget paramWidget);
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\model\DesktopModelListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */