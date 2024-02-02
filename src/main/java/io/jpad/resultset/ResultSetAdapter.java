package io.jpad.resultset;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.*;
import com.timestored.sqldash.chart.SimpleResultSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public enum ResultSetAdapter
        implements Processor {
    INSTANCE;


    private static final Set<Class<?>> WRAPPER_TYPES;


    private static final int MAX_DEPTH = 4;


    private static final String SKEY = "toString";


    private static final Logger log;


    static {

        log = Logger.getLogger(ResultSetAdapter.class.getName());

        WRAPPER_TYPES = getWrapperTypes();

    }


    @Nullable
    public static KeyedResultSet get(@Nullable Object o) {

        return get(o, 0);

    }


    @Nullable
    private static KeyedResultSet get(@Nullable Object o, int depth) {

        Object r = INSTANCE.process(o, depth);

        return (r instanceof KeyedResultSet) ? (KeyedResultSet) r : null;

    }


    public static boolean isWrapperType(Class<?> clazz) {

        return WRAPPER_TYPES.contains(clazz);

    }


    private static Set<Class<?>> getWrapperTypes() {

        Set<Class<?>> ret = new HashSet<>();

        ret.add(Boolean.class);

        ret.add(Character.class);

        ret.add(Byte.class);

        ret.add(Short.class);

        ret.add(Integer.class);

        ret.add(Long.class);

        ret.add(Float.class);

        ret.add(Double.class);

        ret.add(Void.class);

        return ret;

    }


    @NotNull
    private static Map<String, Object> convertToMap(@NotNull Object obj, int depth) {

        Class<?> pomclass = obj.getClass();

        Method[] methods = pomclass.getMethods();

        Map<String, Object> map = Maps.newHashMap();

        try {

            for (Method m : methods) {

                int mod = m.getModifiers();

                if (m.getName().startsWith("get") && !m.getName().startsWith("getClass") && !m.getReturnType().equals(void.class) && !Modifier.isStatic(mod)) {

                    Object value = m.invoke(obj);

                    map.put(m.getName().substring(3), value);

                }

            }

        } catch (IllegalAccessException | IllegalArgumentException | java.lang.reflect.InvocationTargetException e) {
        }

        try {

            for (Field f : pomclass.getFields()) {

                int mod = f.getModifiers();

                if (!Modifier.isPrivate(mod) && !Modifier.isStatic(mod))
                    map.put(f.getName(), f.get(obj));

            }

        } catch (IllegalAccessException | IllegalArgumentException e) {
        }

        map.put("toString", obj.toString());

        return map;

    }


    private static KeyedResultSet joinEach(ResultSet leftRS, ResultSet rightRS, String caption) {

        return new CombinedResultSet(leftRS, rightRS, caption);

    }


    private static Collection<Integer> asList(int[] v) {

        List<Integer> l = new ArrayList<>(v.length);

        for (int a : v)

            l.add(Integer.valueOf(a));

        return l;

    }


    private static <T> KeyedResultSet rs(String colName, Collection<T> c) {

        return new SimpleCollectionResultSet<>(colName, c);

    }


    private static KeyedResultSet getNativeArrayResultSet(Object o) {

        if (o instanceof int[])
            return rs("int[]", asList((int[]) o));

        if (o instanceof long[])
            return rs("long[]", Longs.asList((long[]) o));

        if (o instanceof short[])
            return rs("short[]", Shorts.asList((short[]) o));

        if (o instanceof double[])
            return rs("double[]", Doubles.asList((double[]) o));

        if (o instanceof float[])
            return rs("float[]", Floats.asList((float[]) o));

        if (o instanceof boolean[])
            return rs("boolean[]", Booleans.asList((boolean[]) o));

        if (o instanceof byte[])
            return rs("byte[]", Bytes.asList((byte[]) o));

        if (o instanceof char[])
            return rs("char[]", Chars.asList((char[]) o));

        return null;

    }


    public Object process(Object o, int depth) {

        if (o == null) {

            return "";

        }

        if (depth == 4) {

            return o;

        }

        KeyedResultSet rs = null;

        Class<? extends Object> cls = o.getClass();

        String oClassName = cls.getSimpleName();

        if (o instanceof ResultSet) {

            try {

                CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();

                crs.populate((ResultSet) o);

                rs = new KeyedResultSetWrapper(crs, 0, "ResultSet");

            } catch (SQLException e) {

                e.printStackTrace();

            }

        } else if ((rs = getNativeArrayResultSet(o)) == null) {

            if (o instanceof String[]) {

                rs = rs("String[]", Arrays.asList((Object[]) o));

            } else if (o instanceof Object[]) {

                rs = getRS(Arrays.asList((Object[]) o), oClassName, INSTANCE, depth);

            } else if (o instanceof Iterable) {

                Iterable<?> c = (Iterable) o;

                Class<?> ctype = CollectionUtils.getType(c);

                if (!c.iterator().hasNext()) {

                    rs = new EmptyResultSet(oClassName);

                } else if (SimpleCollectionResultSet.isCompatible(ctype)) {

                    String colName = oClassName + "<" + ctype.getSimpleName() + ">";

                    rs = new SimpleCollectionResultSet(colName, c);

                } else {

                    rs = getRS(Lists.newArrayList(c), oClassName, INSTANCE, depth);

                }

            } else if (o instanceof Map) {

                Map m = (Map) o;

                if (m.isEmpty()) {

                    rs = new EmptyResultSet(oClassName, 1, "Key", "Value");

                } else {

                    List keys = new ArrayList();

                    List values = new ArrayList();

                    for (Object mo : m.entrySet()) {

                        Map.Entry me = (Map.Entry) mo;

                        keys.add(me.getKey());

                        values.add(me.getValue());

                    }

                    KeyedResultSet left = get(keys, depth);

                    KeyedResultSet right = get(values, depth);

                    if (left == null || right == null) {

                        log.log(Level.WARNING, "Failed at converting object->map.");

                    } else {

                        rs = joinEach(left, right, oClassName);

                    }

                }

            } else if (!isWrapperType(cls) && !cls.equals(String.class) && !cls.equals(Object.class) && !cls.getName().startsWith("java.")) {

                Map<String, Object> m = convertToMap(o, depth);

                if (m.size() > 1) {

                    m.remove("toString");

                }

                rs = get(m, depth + 1);

            }

        }

        return (rs != null) ? rs : o;

    }


    private <T> KeyedResultSet getRS(Collection<T> c, String caption, Processor processor, int depth) {

        Class<?> typ = CollectionUtils.getType(c);

        boolean showAsColumnPerGetter = false;

        if (!typ.equals(Object.class) && !c.isEmpty()) {

            T firstOne = c.iterator().next();

            if (!firstOne.getClass().isArray() && !(firstOne instanceof Iterable))
                showAsColumnPerGetter = true;

        }

        if (showAsColumnPerGetter)
            return new SameObjectCollectionResultSet(c, caption, processor, depth);

        return new DiffObjectCollectionResultSet(c, caption, processor, depth);

    }


    private KeyedResultSet mapEveryObjectPropertyToColumn(Iterable c, int depth) {

        Class<?> typ = CollectionUtils.getType(c);

        List<Map<String, Object>> fieldsToValues = Lists.newArrayList();

        boolean allObjectsHaveMoreThanJustToString = true;

        int size = 0;

        for (Object o : c) {

            size++;

            if (o != null) {

                Map<String, Object> m = convertToMap(o, depth);

                fieldsToValues.add(m);

                //int i = allObjectsHaveMoreThanJustToString & ((m.size() > 1) ? 1 : 0);

                continue;

            }

            fieldsToValues.add(null);

            allObjectsHaveMoreThanJustToString = false;

        }

        Set<String> allFields = new HashSet<>();

        for (Map<String, Object> m : fieldsToValues) {

            if (m != null)
                allFields.addAll(m.keySet());

        }

        if (allObjectsHaveMoreThanJustToString)
            allFields.remove("toString");

        List<String> colNames = new ArrayList<>(allFields);

        Collections.sort(colNames);

        Object[][] colValues = new Object[colNames.size()][];

        for (int col = 0; col < colNames.size(); col++) {

            colValues[col] = new Object[size];

            int r = 0;

            for (Map<String, Object> m : fieldsToValues) {

                if (m == null) {

                    colValues[col][r] = "";

                } else {

                    Object v = m.get(colNames.get(col));

                    colValues[col][r] = process(v, depth + 1);

                }

                r++;

            }

        }

        SimpleResultSet rs = new SimpleResultSet(colNames.toArray(new String[0]), colValues);

        return new KeyedResultSetWrapper(rs, 0, typ.getSimpleName());

    }

}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\resultset\ResultSetAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */