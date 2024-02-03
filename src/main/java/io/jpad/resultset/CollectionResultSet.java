package io.jpad.resultset;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.timestored.sqldash.chart.BaseResultSet;

import java.sql.SQLException;
import java.util.List;

abstract class CollectionResultSet<T>
        extends BaseResultSet {
    protected final List<T> l;
    protected int i;

    public CollectionResultSet(Iterable<T> c) {

        this.l = Lists.newArrayList((Iterable) Preconditions.checkNotNull(c));
    }

    public boolean next() throws SQLException {
        return (++this.i < this.l.size());
    }

    public boolean previous() throws SQLException {
        return (--this.i >= 0);
    }

    public void beforeFirst() throws SQLException {
        this.i = -1;
    }

    public boolean isBeforeFirst() throws SQLException {
        return (this.i < 0);
    }

    public boolean isAfterLast() throws SQLException {
        return (this.i >= this.l.size());
    }

    public boolean isFirst() throws SQLException {
        return (this.i == 0);
    }

    public boolean isLast() throws SQLException {
        return (this.i == this.l.size() - 1);
    }

    public void afterLast() throws SQLException {
        this.i = this.l.size();
    }

    public boolean first() throws SQLException {
        this.i = 0;
        return this.l.isEmpty();
    }

    public boolean last() throws SQLException {
        this.i = this.l.size() - 1;
        return this.l.isEmpty();
    }

    public int getRow() throws SQLException {

        return this.i + 1;
    }

    public boolean absolute(int row) throws SQLException {

        this.i = row + 1;

        return (this.i >= 0 && this.i < this.l.size());
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\resultset\CollectionResultSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */