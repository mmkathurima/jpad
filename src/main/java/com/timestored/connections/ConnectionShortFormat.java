package com.timestored.connections;

import com.google.common.base.Preconditions;

import java.util.*;

public class ConnectionShortFormat {
    public static List<ParseResult> parse(String serverEntries, JdbcTypes defaultServerType, JdbcTypes[] permittedJdbcTypes) {
        Preconditions.checkNotNull(defaultServerType);
        Preconditions.checkNotNull(permittedJdbcTypes);
        Set<JdbcTypes> permittedTypes = new HashSet<>(Arrays.asList(permittedJdbcTypes));
        Preconditions.checkArgument(permittedTypes.contains(defaultServerType));

        String[] serverLines = serverEntries.split("\n");
        List<ParseResult> r = new ArrayList<>(serverLines.length);

        for (String serverLine : serverLines) {

            String l = serverLine.trim();
            if (l.length() > 0) {
                r.add(parseLine(defaultServerType, permittedTypes, l));
            }
        }

        return r;
    }

    private static ParseResult parseLine(JdbcTypes defaultServerType, Set<JdbcTypes> permittedTypes, String line) {
        ParseResult parseResult;
        String l = line;
        String name = "";
        try {
            int atPos = l.indexOf("@");
            if (atPos > -1) {
                name = l.substring(0, atPos).trim();
                l = l.substring(atPos + 1).trim();
            }

            String[] p = l.split(":");

            if (p.length >= 2) {

                String host = p[0];
                int port = Integer.parseInt(p[1]);

                int lastSlash = host.lastIndexOf("/");
                if (lastSlash > -1) {
                    name = host + ":" + port;
                    host = host.substring(lastSlash + 1);
                }

                String username = "";
                String password = "";
                JdbcTypes jtype = defaultServerType;

                int hashPos = host.indexOf("#");
                if (hashPos > -1) {
                    jtype = JdbcTypes.valueOf(host.substring(0, hashPos).trim());
                    if (!permittedTypes.contains(jtype)) {
                        throw new IllegalArgumentException("Illegal JDBC connection type");
                    }
                    host = host.substring(hashPos + 1).trim();
                }

                if (p.length >= 3) {
                    username = p[2].trim();
                }
                if (p.length >= 4) {
                    password = p[3].trim();
                }

                parseResult = new ParseResult(l, new ServerConfig(host, port, username, password, name, jtype));
            } else {

                parseResult = new ParseResult(l, "No : Found so could not parse hostname:port");
            }
        } catch (NumberFormatException nfe) {
            parseResult = new ParseResult(l, "Error parsing number in definition");
        } catch (IllegalArgumentException iae) {
            parseResult = new ParseResult(l, "Error parsing: " + iae.getMessage());
        }
        return parseResult;
    }

    public static String compose(List<ServerConfig> serverConfs, JdbcTypes defaultServerType) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < serverConfs.size(); i++) {
            if (i > 0) {
                sb.append("\r\n");
            }
            ServerConfig sc = serverConfs.get(i);
            String hp = sc.getHost() + ":" + sc.getPort();

            if (!sc.getName().equals(hp)) {
                sb.append(sc.getName()).append("@");
            }
            if (!defaultServerType.equals(sc.getJdbcType())) {
                sb.append(sc.getJdbcType().name()).append("#");
            }

            sb.append(hp);

            if (!sc.getUsername().isEmpty() || !sc.getPassword().isEmpty()) {
                sb.append(":").append(sc.getUsername());
            }
            if (!sc.getPassword().isEmpty()) {
                sb.append(":").append(sc.getPassword());
            }
        }

        return sb.toString();
    }

    public static class ParseResult {
        public final ServerConfig serverConfig;
        public final String report;
        public final String originalLine;

        public ParseResult(String originalLine, ServerConfig serverConfig) {
            this(originalLine, serverConfig, "");
        }

        public ParseResult(String originalLine, String error) {
            this(originalLine, null, error);
        }

        private ParseResult(String originalLine, ServerConfig serverConfig, String error) {
            this.serverConfig = serverConfig;
            this.report = error;
            this.originalLine = Preconditions.checkNotNull(originalLine);
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\connections\ConnectionShortFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */