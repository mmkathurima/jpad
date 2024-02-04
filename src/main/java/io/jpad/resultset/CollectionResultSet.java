package io.jpad.resultset;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.timestored.sqldash.chart.BaseResultSet;

import java.util.List;

abstract class CollectionResultSet<T>
        extends BaseResultSet {
    protected final List<T> l;
    protected int i;

    public CollectionResultSet(Iterable<T> c) {

        this.l = Lists.newArrayList((Iterable) Preconditions.checkNotNull(c));
    }

    public boolean next() {
        return (++this.i < this.l.size());
    }

    public boolean previous() {
        return (--this.i >= 0);
    }

    public void beforeFirst() {
        this.i = -1;
    }

    public boolean isBeforeFirst() {
        return (this.i < 0);
    }

    public boolean isAfterLast() {
        return (this.i >= this.l.size());
    }

    public boolean isFirst() {
        return (this.i == 0);
    }

    public boolean isLast() {
        return (this.i == this.l.size() - 1);
    }

    public void afterLast() {
        this.i = this.l.size();
    }

    public boolean first() {
        this.i = 0;
        return this.l.isEmpty();
    }

    public boolean last() {
        this.i = this.l.size() - 1;
        return this.l.isEmpty();
    }

    public int getRow() {

        return this.i + 1;
    }

    public boolean absolute(int row) {

        this.i = row + 1;

        return (this.i >= 0 && this.i < this.l.size());
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\resultset\CollectionResultSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */