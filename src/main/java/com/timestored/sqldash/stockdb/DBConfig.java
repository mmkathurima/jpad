package com.timestored.sqldash.stockdb;

import com.timestored.connections.JdbcTypes;

import java.util.List;
import java.util.Set;

public interface DBConfig {
    Set<JdbcTypes> getSupportedJdbcTypes();

    List<String> toInserts(String paramString, List<OHLCDataPoint> paramList);

    List<String> toInserts(List<Stock> paramList);

    List<String> toLiveInserts(List<BidAsk> paramList);

    List<String> getInitSql();

    String getExistingTablesSql();

    String getTableCountSql(String paramString);

    String getSelectAdjPriceHistorySql(String paramString);

    String getOhlcSQL(String paramString, int paramInt);

    String getMarketCapVolSQL();

    String getStockEpsSQL();

    String getWeeklyMonthlyVolume();
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\stockdb\DBConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */