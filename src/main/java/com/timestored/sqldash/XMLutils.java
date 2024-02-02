package com.timestored.sqldash;


public class XMLutils {
    public static final String HEADER = "<?xml version='1.0'?>";

    public static String getTextByTagName(String xml, String tagName) {
        int st = xml.indexOf("<" + tagName);
        String endTag = "</" + tagName + ">";
        int end = xml.indexOf(endTag);
        return xml.substring(st, end + endTag.length());
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\XMLutils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */