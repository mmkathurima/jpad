package com.timestored.sqldash.chart;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.timestored.connections.JdbcTypes;
import com.timestored.sqldash.theme.DBIcons;
import com.timestored.theme.Icon;
import net.sf.jtreemap.swing.JTreeMap;
import net.sf.jtreemap.swing.TreeMapNode;
import net.sf.jtreemap.swing.TreeMapNodeBuilder;
import net.sf.jtreemap.swing.ValuePercent;
import net.sf.jtreemap.swing.provider.RedGreenColorProvider;
import net.sf.jtreemap.swing.provider.ZoomPopupMenu;

import javax.swing.BorderFactory;
import java.awt.Component;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum HeatMapViewStrategy
        implements ViewStrategy {
    INSTANCE;

    private static final String[] FORMATA;

    static {
        FORMATA = new String[]{"Starting from the left each string column is taken as one nesting level", "The first numerical column will be taken as size, the second as colour."};
    }

    private static TreeMapNode buildStringColTree(List<ChartResultSet.StringyCol> stringCols, double[] weights, double[] values) {
        Preconditions.checkArgument((stringCols.size() > 0));
        TreeMapNodeBuilder builder = new TreeMapNodeBuilder();
        TreeMapNode rootNode = builder.buildBranch("Root", null);

        int levels = stringCols.size() - 1;

        if (levels >= 0) {
            List<Map<String, TreeMapNode>> levelMaps = Lists.newArrayListWithCapacity(levels);
            for (int l = 0; l < levels; l++) {
                levelMaps.add(new HashMap<String, TreeMapNode>());
            }

            for (int row = 0; row < weights.length; row++) {
                TreeMapNode parent = rootNode;
                for (int col = 0; col < stringCols.size(); col++) {

                    List<Object> curLabels = stringCols.get(col).getVals();
                    boolean atBranch = (col != stringCols.size() - 1);
                    if (atBranch) {

                        String branchName = stringCols.get(col).getVals().get(row).toString();
                        TreeMapNode branchNode = (TreeMapNode) ((Map) levelMaps.get(col)).get(branchName);
                        if (branchNode == null) {
                            branchNode = builder.buildBranch(branchName, parent);
                            levelMaps.get(col).put(branchName, branchNode);
                        }
                        parent = branchNode;
                    } else {

                        builder.buildLeaf(curLabels.get(row).toString(), weights[row], new ValuePercent(values[row]), parent);
                    }
                }
            }
        }

        return builder.getRoot();
    }

    public UpdateableView getView(ChartTheme theme) {
        return new HardRefreshUpdateableView(new HardRefreshUpdateableView.ViewGetter() {

            public Component getView(ResultSet resultSet, ChartResultSet chartResultSet) throws ChartFormatException {
                JTreeMap treeMap = new JTreeMap(HeatMapViewStrategy.this.createTreeMapDataset(chartResultSet));
                treeMap.setBorder(BorderFactory.createEtchedBorder(1));
                treeMap.setColorProvider(new RedGreenColorProvider(treeMap));
                new ZoomPopupMenu(treeMap);

                return treeMap;
            }
        });
    }

    public Icon getIcon() {
        return DBIcons.CHART_HEATMAP;
    }

    private TreeMapNode createTreeMapDataset(ChartResultSet colResultSet) throws ChartFormatException {
        ImmutableList<ChartResultSet.StringyCol> immutableList = ImmutableList.of();
        List<ChartResultSet.NumericCol> numColumns = colResultSet.getNumericColumns();

        if (numColumns.size() < 1) {
            throw new ChartFormatException("There must be atleast one number column.");
        }

        List<ChartResultSet.StringyCol> stringCols = colResultSet.getStringyColumns();
        if (stringCols.size() == 0) {
            immutableList = ImmutableList.of(colResultSet.getRowLabels());
        }

        double[] weights = numColumns.get(0).getDoubles();
        double[] values = weights;
        if (numColumns.size() > 1) {
            values = numColumns.get(1).getDoubles();
        }

        return buildStringColTree(immutableList, weights, values);
    }

    public String getDescription() {
        return "Heat Map";
    }

    public String getFormatExplainationHtml() {
        return "A HeatMap works best with 1+ string columns.<ol><li>" + Joiner.on("</li><li>").join(FORMATA) + "</li></ol>";
    }

    public String getFormatExplaination() {
        return "A HeatMap works best with 1+ string columns.\r\n" + Joiner.on("\r\n").join(FORMATA);
    }

    public List<ExampleView> getExamples() {
        ExampleView ev = new ExampleView("Country GDP's", "The continent column is a top-level branch, the country column becomes leafs.The first two columns are GDP and GDP per Capita which become the size and color of the leafs respectively.", ExampleTestCases.COUNTRY_STATS);

        return ImmutableList.of(ev);
    }

    public String getQueryEg(JdbcTypes jdbcType) {
        if (jdbcType.equals(JdbcTypes.KDB)) {
            return ExampleTestCases.COUNTRY_STATS.getKdbQuery();
        }
        return null;
    }

    public Component getControlPanel() {
        return null;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\HeatMapViewStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */