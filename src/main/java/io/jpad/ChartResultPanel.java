package io.jpad;

import com.timestored.sqldash.ChartControlPanel;
import com.timestored.sqldash.model.ChartWidget;
import com.timestored.sqldash.model.Queryable;
import com.timestored.theme.Theme;
import io.jpad.model.PanelResultRenderer;
import io.jpad.model.RunResult;
import io.jpad.resultset.KeyedResultSet;
import io.jpad.scratch.CapturedObject;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.io.IOException;
import java.util.List;


public class ChartResultPanel
        extends PanelResultRenderer {
    private static final long serialVersionUID = 1L;
    private static final int PADDING = 10;
    private final ChartWidget app;

    public ChartResultPanel() {
        super("chartResult", Theme.CIcon.CHART_CURVE.get16());
        this.app = new ChartWidget();
        resetContent();
    }

    private void resetContent() {
        removeAll();
        JPanel configPanel = Theme.getVerticalBoxPanel();
        configPanel.add(new ChartControlPanel(this.app));

        setLayout(new BorderLayout());
        add(configPanel, "West");

        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.add(this.app.getPanel(), "Center");
        add(p, "Center");
        repaint();
    }


    public void resultReturned(RunResult runResult) {
        this.app.setIgnoreConfigChanges(true);
        Queryable q = new Queryable("", "");
        this.app.setQueryable(q);
        this.app.setIgnoreConfigChanges(false);


        List<CapturedObject> dumps = runResult.getDumps();
        if (dumps.size() > 0) {

            KeyedResultSet keyedResultSet = dumps.get(dumps.size() - 1).getResultSet();
            if (keyedResultSet != null) {
                this.app.tabChanged(q, keyedResultSet);
            }
        } else {
            this.app.queryError(q, new IOException("Query No Show"));
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\ChartResultPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */