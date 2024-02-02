package com.timestored.connections;

import com.google.common.base.Preconditions;
import com.timestored.kdb.KdbTestHelper;

import java.io.IOException;
import java.sql.SQLException;


public class DBTestRunnerFactory {
    public static DBTestRunner getDbRunner(JdbcTypes jdbcType) {
        Preconditions.checkNotNull(jdbcType);

        if (jdbcType.equals(JdbcTypes.H2))
            return H2DBTestRunner.getInstance();
        if (jdbcType.equals(JdbcTypes.KDB)) {
            return KdbDBTestRunner.INSTANCE;
        }

        return null;
    }

    private static class KdbDBTestRunner
            implements DBTestRunner {
        private static final KdbDBTestRunner INSTANCE = new KdbDBTestRunner();

        public ConnectionManager start() throws SQLException {
            try {
                return KdbTestHelper.getNewConnectedMangager();
            } catch (IOException e) {
                throw new SQLException(e);
            } catch (InterruptedException e) {
                throw new SQLException(e);
            }
        }

        public void stop() {
            try {
                KdbTestHelper.killAnyOpenProcesses();
            } catch (IOException e) {
            }
        }

        public ServerConfig getServerConfig() {
            return KdbTestHelper.SERVER_CONFIG;
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\connections\DBTestRunnerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */