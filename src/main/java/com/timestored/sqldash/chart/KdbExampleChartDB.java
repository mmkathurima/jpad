package com.timestored.sqldash.chart;

import com.google.common.base.Preconditions;
import com.timestored.connections.JdbcTypes;
import com.timestored.sqldash.exampledb.ExampleChartDB;
import com.timestored.sqldash.exampledb.ExampleChartQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class KdbExampleChartDB
        implements ExampleChartDB {
    private static final ExampleChartDB INSTANCE = new KdbExampleChartDB();

    public static ExampleChartDB getInstance() {
        return INSTANCE;
    }

    public String getName() {
        return "Self-Contained Queries";
    }

    public String getDescription() {
        return "Database where no initialisation is needed and each chart example is self-cointained.";
    }

    public List<String> getInitSQL(boolean withComments) {
        return Collections.emptyList();
    }

    public JdbcTypes getDbType() {
        return JdbcTypes.KDB;
    }

    public List<ExampleChartQuery> getQueries() {
        List<ExampleChartQuery> l = new ArrayList<ExampleChartQuery>();
        for (ViewStrategy vs : ViewStrategyFactory.getStrategies()) {
            for (ExampleView ev : vs.getExamples()) {
                l.add(new ExampleWrapper(ev, vs));
            }
        }

        return l;
    }

    private static class ExampleWrapper
            implements ExampleChartQuery {
        private final ExampleView ev;
        private final ViewStrategy vs;

        ExampleWrapper(ExampleView exampleView, ViewStrategy viewStrategy) {
            this.ev = Preconditions.checkNotNull(exampleView);
            this.vs = Preconditions.checkNotNull(viewStrategy);
        }

        public String getName() {
            return this.ev.getName();
        }

        public String getDescription() {
            return this.ev.getDescription();
        }

        public String getSqlQuery() {
            return this.ev.getTestCase().getKdbQuery();
        }

        public ViewStrategy getSupportedViewStrategy() {
            return this.vs;
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\KdbExampleChartDB.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */