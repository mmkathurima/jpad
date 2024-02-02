package com.timestored.sqldash;

import bibliothek.gui.Dockable;
import bibliothek.gui.dock.DockFactory;
import bibliothek.gui.dock.layout.LocationEstimationMap;
import bibliothek.gui.dock.perspective.PerspectiveDockable;
import bibliothek.gui.dock.perspective.PerspectiveElement;
import bibliothek.gui.dock.station.support.PlaceholderStrategy;

import java.util.Map;


public abstract class AbstractDockFactory
        implements DockFactory<Dockable, PerspectiveElement, MyLayout> {
    public MyLayout getPerspectiveLayout(PerspectiveElement arg0, Map<PerspectiveDockable, Integer> arg1) {
        return null;
    }


    public void setLayout(Dockable arg0, MyLayout arg1, Map<Integer, Dockable> arg2, PlaceholderStrategy arg3) {
        setLayout(arg0, arg1, arg3);
    }


    public void setLayout(Dockable arg0, MyLayout arg1, PlaceholderStrategy arg2) {
    }


    public void estimateLocations(MyLayout arg0, LocationEstimationMap arg1) {
    }


    public Dockable layout(MyLayout ml, Map<Integer, Dockable> m, PlaceholderStrategy p) {
        return layout(ml, p);
    }


    public PerspectiveElement layoutPerspective(MyLayout arg0, Map<Integer, PerspectiveDockable> arg1) {
        return null;
    }

    public void layoutPerspective(PerspectiveElement arg0, MyLayout arg1, Map<Integer, PerspectiveDockable> arg2) {
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\AbstractDockFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */