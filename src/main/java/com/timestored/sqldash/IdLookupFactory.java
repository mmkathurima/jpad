package com.timestored.sqldash;

import bibliothek.gui.Dockable;
import bibliothek.gui.dock.DockFactory;
import bibliothek.gui.dock.perspective.PerspectiveElement;
import bibliothek.gui.dock.station.support.PlaceholderStrategy;
import bibliothek.util.xml.XElement;
import com.google.common.base.Preconditions;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public abstract class IdLookupFactory
        extends AbstractDockFactory
        implements DockFactory<Dockable, PerspectiveElement, MyLayout> {
    private static final String ID_TAG = "idl";
    private final String factoryId;

    public IdLookupFactory(String factoryId) {
        this.factoryId = Preconditions.checkNotNull(factoryId);
    }

    public String getID() {
        return this.factoryId;
    }

    public MyLayout getLayout(Dockable dd, Map<Dockable, Integer> a) {
        return new MyLayout(this.getId(dd));
    }

    public Dockable layout(MyLayout ml, PlaceholderStrategy ps) {
        return this.getDockable(ml.getId());
    }

    public MyLayout read(DataInputStream i, PlaceholderStrategy arg1) throws IOException {
        return new MyLayout(i.readInt());
    }

    public MyLayout read(XElement x, PlaceholderStrategy ps) {
        return new MyLayout(x.getInt("idl"));
    }

    public void write(MyLayout ml, DataOutputStream o) throws IOException {
        o.writeInt(ml.getId());
    }

    public void write(MyLayout ml, XElement x) {
        x.addInt("idl", ml.getId());
    }

    public abstract int getId(Dockable paramDockable);

    public abstract Dockable getDockable(int paramInt);
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\IdLookupFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */