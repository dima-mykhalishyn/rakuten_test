package org.test.project.support.db;

import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Custom {@link QueryRunner}
 * @author dmikhalishin@provectus-it.com
 * @see QueryRunner
 */
public class CustomQueryRunner extends QueryRunner {

    public CustomQueryRunner(DataSource ds) {
        super(ds);
    }

    /**
     * Method that return generated Id for insert operation
     * @param sql the SQL string. Cannot be {@code null}
     * @return generated ID
     */
    public long insert(final String sql, final Object... params) throws SQLException {
        Connection conn = this.prepareConnection();

        if (conn == null) {
            throw new SQLException("Null connection");
        }

        if (sql == null) {
            close(conn);
            throw new SQLException("Null SQL statement");
        }

        PreparedStatement stmt = null;
        long insertedId = -1;

        try {
            stmt = this.prepareStatement(conn, sql,Statement.RETURN_GENERATED_KEYS);
            this.fillStatement(stmt, params);
            stmt.executeUpdate();

            final ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                insertedId = generatedKeys.getLong(1);
            } else {
                throw new SQLException("No generated key obtained.");
            }

        } catch (SQLException e) {
            this.rethrow(e, sql, params);

        } finally {
            close(stmt);
            close(conn);
        }

        return insertedId;
    }

    private PreparedStatement prepareStatement(final Connection conn, final String sql, final int autoGeneratedKeys) throws SQLException {
        return conn.prepareStatement(sql, autoGeneratedKeys);
    }
}