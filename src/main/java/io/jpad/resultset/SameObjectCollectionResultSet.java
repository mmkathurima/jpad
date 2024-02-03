package io.jpad.resultset;

import com.google.common.base.Preconditions;
import com.timestored.misc.IOUtils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

class SameObjectCollectionResultSet<T>
        extends CollectionResultSet<T>
        implements KeyedResultSet {
    private static final Logger LOG = Logger.getLogger(SameObjectCollectionResultSet.class.getName());

    private final SimpleResultSetMetaData rsmd;
    private final List<String> colNames = new ArrayList<>();
    private final List<Method> methods = new ArrayList<>();

    private final String caption;

    private final Processor processor;

    private final int depth;

    public SameObjectCollectionResultSet(Collection<T> c, String caption, Processor processor, int depth) {

        super(c);

        this.processor = Preconditions.checkNotNull(processor);

        this.depth = depth;

        Class<?> t = CollectionUtils.getType(c);

        if (caption.isEmpty()) {

            this.caption = c.getClass().getSimpleName() + "<" + t.getSimpleName() + ">";
        } else {

            this.caption = caption;
        }

        List<Integer> colTypes = new ArrayList<>();

        try {

            for (PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(t).getPropertyDescriptors()) {

                Method m = propertyDescriptor.getReadMethod();

                if (m != null) {

                    Class<?> rt = m.getReturnType();

                    Integer sqlType = SimpleCollectionResultSet.getSqlType(rt);

                    if (sqlType == null) {

                        sqlType = Integer.valueOf(12);
                    }

                    if (!m.getName().equals("getClass")) {

                        this.methods.add(m);

                        m.setAccessible(true);

                        String s = m.getName();

                        this.colNames.add(s.startsWith("get") ? s.substring(3) : s);

                        colTypes.add(Integer.valueOf((sqlType != null) ? sqlType.intValue() : 12));
                    }
                }
            }
        } catch (IntrospectionException e) {
        }

        this.rsmd = new SimpleResultSetMetaData(this.colNames, colTypes);
    }

    public int getNumberOfKeyColumns() {
        return 0;
    }

    public String getCaption() {
        return this.caption;
    }

    public ResultSetMetaData getMetaData() throws SQLException {

        return this.rsmd;
    }

    public Object getObject(int columnIndex) throws SQLException {

        Method m = this.methods.get(columnIndex - 1);

        Object o = this.l.get(this.i);

        try {

            return this.processor.process(m.invoke(o), this.depth + 1);
        } catch (IllegalAccessException | IllegalArgumentException |
                 java.lang.reflect.InvocationTargetException e) {

            LOG.warning(IOUtils.toString(e));

            return null;
        }
    }

    public int findColumn(String columnLabel) throws SQLException {

        return 1 + this.colNames.indexOf(columnLabel);
    }

    public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {

        return null;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\resultset\SameObjectCollectionResultSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */