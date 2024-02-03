package com.timestored.sqldash.model;

import com.google.common.base.Joiner;
import com.timestored.connections.JdbcTypes;

import java.util.*;

public class QueryTranslator {
    public static final char ARGW = '$';
    private final Map<String, Object> keyToVals;

    public QueryTranslator(Map<String, Object> keyToVals) {
        this.keyToVals = keyToVals;
    }

    private static String convertValue(Object value, JdbcTypes jdbcType) {
        String r;
        if (value == null) {
            return "";
        }

        Object v = value;
        if (value instanceof int[]) {
            v = Arrays.asList(new int[][]{(int[]) value});
        } else if (value instanceof float[]) {
            v = Arrays.asList(new float[][]{(float[]) value});
        } else if (value instanceof double[]) {
            v = Arrays.asList(new double[][]{(double[]) value});
        } else if (value instanceof long[]) {
            v = Arrays.asList(new long[][]{(long[]) value});
        } else if (value instanceof String[]) {
            v = Arrays.asList((String[]) value);
        }

        boolean isKDB = jdbcType.equals(JdbcTypes.KDB);

        String t = isKDB ? ";" : ",";

        if (v instanceof List) {
            List<?> l = (List) v;
            if (isAllNumbers(l)) {
                if (isKDB && l.size() == 1) {
                    r = "(enlist " + Joiner.on(t).join(l) + ")";
                } else {
                    r = "(" + Joiner.on(t).join(l) + ")";
                }
            } else {

                StringBuilder sb = new StringBuilder("(");
                if (l.size() == 1 && isKDB) {
                    sb.append("enlist ").append(wrapString(jdbcType, "" + l.get(0)));
                } else {
                    boolean firstEntry = true;
                    for (Object o : l) {
                        if (!firstEntry) {
                            sb.append(t);
                        }
                        sb.append(wrapString(jdbcType, "" + o));
                        firstEntry = false;
                    }
                }
                r = sb.append(")").toString();
            }
        } else {
            r = "" + (isNumber(v) ? (String) v : wrapString(jdbcType, "" + v));
        }
        return r;
    }

    private static String wrapString(JdbcTypes jdbcType, String s) {
        boolean isKDB = jdbcType.equals(JdbcTypes.KDB);
        String r = "";
        if (isKDB) {

            if (s.length() < 2) {
                r = "enlist ";
            }
            r = r + "\"" + s.replace("\"", "\\\"") + "\"";
        } else {
            r = "'" + s.replace("'", "''") + "'";
        }
        return r;
    }

    private static boolean isAllNumbers(List<?> l) {
        for (Object o : l) {
            if (!isNumber(o)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isNumber(Object o) {
        if (o instanceof Number) {
            return true;
        }
        try {
            Double.parseDouble("" + o);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static Collection<String> extractArgs(String qry) {
        Set<String> l = null;
        if (qry != null && qry.length() >= 2) {
            char[] a = qry.toCharArray();
            for (int i = 0; i < a.length - 1; i++) {
                if (a[i] == '$') {
                    if (i + 1 < a.length && a[i + 1] == '$') {
                        i++;
                    } else {
                        i++;
                        if (i < a.length) {
                            int st = i;
                            while (i < a.length && Character.isLetterOrDigit(a[i])) {
                                i++;
                            }
                            if (l == null) {
                                l = new HashSet<String>();
                            }
                            l.add(qry.substring(st, i));
                        }
                    }
                }
            }
        }
        if (l != null) {
            return l;
        }
        return Collections.emptySet();
    }

    public static Collection<Queryable> filterByKeys(Collection<Queryable> qs, Set<String> keySet) {
        List<Queryable> r = new ArrayList<Queryable>();
        for (Queryable q : qs) {
            for (String a : keySet) {
                if (extractArgs(q.getQuery()).contains(a)) {
                    r.add(q);
                }
            }
        }

        return r;
    }

    public String translate(String qry, JdbcTypes jdbcTypes) {
        if (qry != null && qry.contains("$")) {
            StringBuilder sb = new StringBuilder();

            if (qry.length() >= 2) {
                char[] a = qry.toCharArray();

                for (int i = 0; i < a.length; i++) {

                    if (a[i] == '$' && i + 1 < a.length) {
                        boolean nextIsDollar = (i + 1 < a.length && a[i + 1] == '$');
                        if (nextIsDollar) {
                            i++;
                            sb.append('$');
                        } else {

                            int st = ++i;
                            while (i < a.length && Character.isLetterOrDigit(a[i])) {
                                i++;
                            }
                            String key = qry.substring(st, i);
                            if (!this.keyToVals.containsKey(key)) {
                                return null;
                            }
                            sb.append(convertValue(this.keyToVals.get(key), jdbcTypes));
                            i--;
                        }
                    } else {
                        sb.append(a[i]);
                    }
                }
            }

            return sb.toString();
        }

        return qry;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\model\QueryTranslator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */