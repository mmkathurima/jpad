package io.jpad.resultset;

import java.sql.ResultSet;

public interface KeyedResultSet extends ResultSet {
    int getNumberOfKeyColumns();

    String getCaption();
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\resultset\KeyedResultSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */