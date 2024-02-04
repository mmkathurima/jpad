package com.timestored.sqldash.stockdb;

import com.timestored.connections.ServerConfig;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class DemoFactory {
    private static final Collection<DBConfig> DB_CONFIGS = Arrays.asList(KdbConfig.INSTANCE, H2DBConfig.INSTANCE);

    public static Collection<DBConfig> getDBConfigs() {
        return DB_CONFIGS;
    }

    public static FinanceDataDemo getFinanceDataDemo(ServerConfig serverConfig) throws IOException {
        try {
            DBConfig dbConfig;

            switch (serverConfig.getJdbcType()) {
                case KDB:
                    dbConfig = KdbConfig.INSTANCE;

                    return new FinanceDataDemo(dbConfig, serverConfig);
                case H2:
                case MYSQL:
                    dbConfig = H2DBConfig.INSTANCE;
                    return new FinanceDataDemo(dbConfig, serverConfig);
            }
            return null;
        } catch (ClassNotFoundException e) {
            throw new IOException("sql driver not found:" + serverConfig.getJdbcType());
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\stockdb\DemoFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */