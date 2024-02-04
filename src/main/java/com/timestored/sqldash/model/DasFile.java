package com.timestored.sqldash.model;

import com.google.common.base.Preconditions;
import com.google.common.io.CharStreams;
import com.thoughtworks.xstream.XStream;
import com.timestored.connections.ConnectionManager;
import com.timestored.connections.ServerConfig;
import com.timestored.sqldash.XMLutils;
import com.timestored.sqldash.forms.FormWidgetDTO;
import com.timestored.sqldash.forms.ListSelectionWidgetDTO;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class DasFile {
    private static final Logger LOG = Logger.getLogger(DasFile.class.getName());
    private static final String XML_FILENAME = "app.xml";
    private static final XStream stream = new XStream();
    public static String XML_ROOT = "dashboard";

    static {
        stream.processAnnotations(DesktopDTO.class);
        stream.processAnnotations(WorkspaceDTO.class);
        stream.processAnnotations(ChartWidgetDTO.class);
        stream.processAnnotations(FormWidgetDTO.class);
        stream.processAnnotations(ListSelectionWidgetDTO.class);
    }

    DesktopDTO desktopDTO;
    List<ServerConfig> connections;

    public DasFile(String xml) {
        this.setFromXml(xml);
    }

    public DasFile(DesktopDTO desktopDTO, List<ServerConfig> connections) {
        this.desktopDTO = desktopDTO;
        if (connections == null) {
            this.connections = Collections.emptyList();
        } else {
            this.connections = connections;
        }
    }

    public DasFile(File dasFile) throws IOException {
        try (ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(dasFile)))) {

            String xml = "";
            ZipEntry ze;
            while ((ze = zin.getNextEntry()) != null) {
                if (ze.getName().equals("app.xml")) {
                    xml = CharStreams.toString(new InputStreamReader(zin, StandardCharsets.UTF_8));
                }
            }
            this.setFromXml(xml);
        }
    }

    public DesktopDTO getDesktopDTO() {
        return this.desktopDTO;
    }

    public List<ServerConfig> getConnections() {
        return this.connections;
    }

    private void setFromXml(String xml) {
        String desktopXML = XMLutils.getTextByTagName(xml, "desktop");
        this.desktopDTO = (DesktopDTO) stream.fromXML(desktopXML);
        List<ServerConfig> c = Collections.emptyList();
        String connXML = XMLutils.getTextByTagName(xml, ConnectionManager.XML_ROOT);
        try {
            c = ConnectionManager.getConnectionsFromXml(connXML);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Could not read connXML = " + connXML, e);
        }
        this.connections = c;
    }

    private String getXML() {
        String s = "<" + XML_ROOT + ">";
        s = s + ConnectionManager.getConnectionsXml(this.connections);
        s = s + stream.toXML(this.desktopDTO);
        return s + "</" + XML_ROOT + ">";
    }

    public void save(File file) throws IOException {
        Preconditions.checkNotNull(file);
        ZipOutputStream zout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(file)));

        ZipEntry entry = new ZipEntry("app.xml");
        byte[] data = this.getXML().getBytes(StandardCharsets.UTF_8);
        entry.setSize(data.length);
        zout.putNextEntry(entry);
        zout.write(data);
        zout.close();
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\model\DasFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */