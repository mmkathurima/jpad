package com.timestored.command;

import javax.swing.Action;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommandManager
        implements CommandProvider {
    private final List<CommandProvider> providers = new ArrayList<CommandProvider>();

    public static Collection<Command> toCommands(List<Action> actions) {
        List<Command> cs = new ArrayList<Command>();
        for (Action a : actions) {
            cs.add(new ActionCommand(a));
        }
        return cs;
    }

    public static Command toCommand(Action action) {
        return new ActionCommand(action);
    }

    public Collection<Command> getCommands() {
        List<Command> r = new ArrayList<Command>();
        for (CommandProvider cp : this.providers) {
            r.addAll(cp.getCommands());
        }
        return r;
    }

    public void registerProvider(CommandProvider commandProvider) {
        this.providers.add(commandProvider);
    }

    public void removeProvider(CommandProvider commandProvider) {
        this.providers.remove(commandProvider);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\command\CommandManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */