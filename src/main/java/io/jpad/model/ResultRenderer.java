package io.jpad.model;

import javax.swing.Action;
import javax.swing.ImageIcon;
import java.awt.Component;
import java.util.List;

public interface ResultRenderer extends JEngineListener {
    String getLatestRendering();

    String getTabName();

    Component getComponent();

    ImageIcon getImageIcon();

    List<Action> getActions();
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\model\ResultRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */