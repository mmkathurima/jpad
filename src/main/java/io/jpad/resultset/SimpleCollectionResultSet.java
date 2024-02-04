package io.jpad.resultset;

import com.google.common.collect.Maps;

import java.sql.ResultSetMetaData;
import java.util.Map;

class SimpleCollectionResultSet<T>
        extends CollectionResultSet<T>
        implements KeyedResultSet {
    private static final Map<Class<?>, Integer> simpleToSqlType = Maps.newHashMap();

    static {

        simpleToSqlType.put(Integer.class, 4);

        simpleToSqlType.put(int.class, 4);

        simpleToSqlType.put(Long.class, -5);

        simpleToSqlType.put(long.class, -5);

        simpleToSqlType.put(Float.class, 7);

        simpleToSqlType.put(float.class, 7);

        simpleToSqlType.put(Double.class, 8);

        simpleToSqlType.put(double.class, 8);

        simpleToSqlType.put(String.class, 12);

        simpleToSqlType.put(Byte.class, 4);

        simpleToSqlType.put(byte.class, 4);

        simpleToSqlType.put(Short.class, 5);

        simpleToSqlType.put(short.class, 5);

        simpleToSqlType.put(Character.class, 1);

        simpleToSqlType.put(char.class, 1);

        simpleToSqlType.put(Boolean.class, -7);

        simpleToSqlType.put(boolean.class, -7);
    }

    private final ResultSetMetaData rsmd;

    public SimpleCollectionResultSet(String colNameAndCaption, Iterable<T> c) {
        super(c);
        Class<?> t = CollectionUtils.getType(c);
        Integer colType = simpleToSqlType.get(t);

        if (colType == null) {
            colType = 12;
        }

        this.rsmd = new SimpleResultSetMetaData(colNameAndCaption, colType);
    }

    public static boolean isCompatible(Class<?> cls) {
        return simpleToSqlType.containsKey(cls);
    }

    public static Integer getSqlType(Class<?> cls) {
        return simpleToSqlType.get(cls);
    }

    public ResultSetMetaData getMetaData() {
        return this.rsmd;
    }

    public Object getObject(int columnIndex) {
        return this.l.get(this.i);
    }

    public int findColumn(String columnLabel) {
        return 1;
    }

    public Object getObject(int columnIndex, Map<String, Class<?>> map) {
        return null;
    }

    public int getNumberOfKeyColumns() {
        return 0;
    }

    public String getCaption() {
        return "";
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\resultset\SimpleCollectionResultSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */