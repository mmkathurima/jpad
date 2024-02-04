package io.jpad.scratch;

import io.jpad.resultset.KeyedResultSet;
import io.jpad.resultset.ResultSetAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class CapturedObject {

    @NotNull
    private final String name;
    @Nullable
    private final Object object;
    @Nullable
    private final KeyedResultSet resultSet;

    public CapturedObject(String name, Object object) {

        this.name = (name == null) ? "" : name;

        this.object = object;

        this.resultSet = ResultSetAdapter.get(object);
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof CapturedObject)) return false;
        CapturedObject other = (CapturedObject) o;
        if (!other.canEqual(this)) return false;
        if (!Objects.equals(this.name, other.name)) return false;
        if (!Objects.equals(this.object, other.object)) return false;
        return Objects.equals(this.resultSet, other.resultSet);
    }

    protected boolean canEqual(Object other) {
        return other instanceof CapturedObject;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        Object $name = this.name;
        result = result * 59 + (($name == null) ? 0 : $name.hashCode());
        Object $object = this.object;
        result = result * 59 + (($object == null) ? 0 : $object.hashCode());
        Object $resultSet = this.resultSet;
        return result * 59 + (($resultSet == null) ? 0 : $resultSet.hashCode());
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @Nullable
    public Object getObject() {
        return this.object;
    }

    @Nullable
    public KeyedResultSet getResultSet() {
        return this.resultSet;
    }

    public String toString() {

        String s = "";

        if (this.name.trim().length() > 0) {

            s = this.name + " = ";
        }

        return s + ((this.object == null) ? "null" : this.object.toString());
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\scratch\CapturedObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */