package io.jpad.model;

import java.beans.ConstructorProperties;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RunConfig {
    private final List<Listener> listeners;
    private String rawCode;
    private String[] args;

    @ConstructorProperties({"rawCode", "args"})
    public RunConfig(String rawCode, String[] args) {

        this.args = new String[0];

        this.listeners = new CopyOnWriteArrayList<>();

        this.rawCode = rawCode;

        this.args = args;
    }

    public RunConfig() {

        this("");
    }

    public RunConfig(String rawCode) {

        this(rawCode, new String[0]);
    }

    public String getRawCode() {
        return this.rawCode;
    }

    public void setRawCode(String rawCode) {
        if (this.rawCode != rawCode) {

            this.rawCode = rawCode;

            for (Listener l : this.listeners) l.change();
        }
    }

    public String[] getArgs() {

        return this.args;
    }

    public void setArgs(String[] args) {

        if (!Arrays.equals(this.args, args)) {

            this.args = args;

            for (Listener l : this.listeners) l.change();
        }
    }

    public boolean addListener(Listener listener) {
        return this.listeners.add(listener);
    }

    public boolean removeListener(Listener listener) {

        return this.listeners.remove(listener);
    }

    public interface Listener {
        void change();
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\model\RunConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */