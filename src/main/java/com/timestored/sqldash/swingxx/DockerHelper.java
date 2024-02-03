package com.timestored.sqldash.swingxx;

import bibliothek.gui.DockFrontend;
import bibliothek.util.xml.XElement;
import bibliothek.util.xml.XIO;

import java.io.IOException;
import java.util.Set;

public class DockerHelper {
    public static String getLayout(DockFrontend frontend) {
        XElement xroot = new XElement("layout");
        frontend.writeXML(xroot);
        String layoutXml = xroot.toString();
        return layoutXml;
    }

    public static void loadLayout(String layoutXml, DockFrontend frontend) {
        if (layoutXml.length() > 0)

            try {

                Set<String> layouts = frontend.getSettings();
                String[] keys = layouts.toArray(new String[layouts.size()]);
                for (String key : keys) {
                    frontend.delete(key);
                }

                frontend.readXML(XIO.read(layoutXml));
            } catch (IOException e1) {
            }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\swingxx\DockerHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */