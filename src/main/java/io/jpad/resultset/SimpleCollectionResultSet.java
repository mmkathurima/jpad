package io.jpad.resultset;

import com.google.common.collect.Maps;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

class SimpleCollectionResultSet<T>
        extends CollectionResultSet<T>
        implements KeyedResultSet {
    private static final Map<Class<?>, Integer> simpleToSqlType = Maps.newHashMap();

    static {

        simpleToSqlType.put(Integer.class, Integer.valueOf(4));

        simpleToSqlType.put(int.class, Integer.valueOf(4));

        simpleToSqlType.put(Long.class, Integer.valueOf(-5));

        simpleToSqlType.put(long.class, Integer.valueOf(-5));

        simpleToSqlType.put(Float.class, Integer.valueOf(7));

        simpleToSqlType.put(float.class, Integer.valueOf(7));

        simpleToSqlType.put(Double.class, Integer.valueOf(8));

        simpleToSqlType.put(double.class, Integer.valueOf(8));

        simpleToSqlType.put(String.class, Integer.valueOf(12));

        simpleToSqlType.put(Byte.class, Integer.valueOf(4));

        simpleToSqlType.put(byte.class, Integer.valueOf(4));

        simpleToSqlType.put(Short.class, Integer.valueOf(5));

        simpleToSqlType.put(short.class, Integer.valueOf(5));

        simpleToSqlType.put(Character.class, Integer.valueOf(1));

        simpleToSqlType.put(char.class, Integer.valueOf(1));

        simpleToSqlType.put(Boolean.class, Integer.valueOf(-7));

        simpleToSqlType.put(boolean.class, Integer.valueOf(-7));
    }

    private final ResultSetMetaData rsmd;

    public SimpleCollectionResultSet(String colNameAndCaption, Iterable<T> c) {

        super(c);

        Class<?> t = CollectionUtils.getType(c);

        Integer colType = simpleToSqlType.get(t);

        if (colType == null) {

            colType = Integer.valueOf(12);
        }

        this.rsmd = new SimpleResultSetMetaData(colNameAndCaption, colType.intValue());
    }

    public static boolean isCompatible(Class<?> cls) {

        return simpleToSqlType.containsKey(cls);
    }

    public static Integer getSqlType(Class<?> cls) {

        return simpleToSqlType.get(cls);
    }

    public ResultSetMetaData getMetaData() throws SQLException {

        return this.rsmd;
    }

    public Object getObject(int columnIndex) throws SQLException {

        return this.l.get(this.i);
    }

    public int findColumn(String columnLabel) throws SQLException {

        return 1;
    }

    public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {

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