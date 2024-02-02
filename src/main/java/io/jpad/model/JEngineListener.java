package io.jpad.model;

import java.awt.Component;

public interface JEngineListener {
    void compiling(JPadCode paramJPadCode);

    void running(JPadCode paramJPadCode);

    void resultReturned(RunResult paramRunResult);

    void compiled(RunResult paramRunResult);

    void displayRequested(Component paramComponent, String paramString);
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\model\JEngineListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */