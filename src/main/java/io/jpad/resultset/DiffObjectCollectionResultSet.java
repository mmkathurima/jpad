package io.jpad.resultset;


import com.google.common.base.Preconditions;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;


class DiffObjectCollectionResultSet<T>
        extends CollectionResultSet<T>
        implements KeyedResultSet {
    private final SimpleResultSetMetaData rsmd;
    private final String caption;
    private final Processor processor;
    private final int depth;


    public DiffObjectCollectionResultSet(Collection<T> c, String caption, Processor processor, int depth) {

        super(c);

        this.processor = Preconditions.checkNotNull(processor);

        this.caption = Preconditions.checkNotNull(caption);

        this.depth = depth;


        this.rsmd = new SimpleResultSetMetaData(caption, 2000);

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

        Object o = this.l.get(this.i);

        return this.processor.process(o, this.depth + 1);

    }


    public int findColumn(String columnLabel) throws SQLException {

        if (this.caption.equals(columnLabel)) {

            return 1;

        }

        throw new SQLException("unknown columnLabel");

    }


    public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {

        return null;

    }

}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\resultset\DiffObjectCollectionResultSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */