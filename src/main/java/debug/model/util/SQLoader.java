/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.util;

import static debug.Pipe.makeConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mkctammi
 */
public class SQLoader {

    public static <T extends Table> List<T> loadTable(T tableType, List<Filter> filters) throws SQLException {
        ArrayList<T> kaikki = new ArrayList();
        Connection conn = makeConnection();
        PreparedStatement ps = conn.prepareStatement(statementString(tableType, filters));
        applyFiltersToStatement(ps, filters);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            T add = tableType.getNewInstance();
            resultRowToTable(rs, add);
            kaikki.add(add);
        }
        conn.close();
        return kaikki;
    }

    private static void applyFiltersToStatement(PreparedStatement ps, List<Filter> filters) throws SQLException {
        if (filters == null) {
            return;
        }
        for (int i = 0; i < filters.size(); i++) {
            Filter filter = filters.get(i);
            if (filter.column.getType() == String.class) {
                ps.setString(i + 1, (String) filter.expectation);
            } else if (filter.column.getType() == Integer.class) {
                ps.setInt(i + 1, (Integer) filter.expectation);
            }
        }
    }

    private static String statementString(Table tableType, List<Filter> filters) {
        String statement = "Select * from " + tableType.getTableName();
        if (filters == null || filters.isEmpty()) {
            return statement;
        }
        statement += " where ";
        for (int i = 0; i < filters.size(); i++) {
            Filter filter = filters.get(i);
            statement += filter.column.getColumnName() + " = ? ";
            if (i == filters.size() - 1) {
                break;
            }
            statement += "and ";
        }
        return statement;
    }

    public static <T extends Table> List<T> loadTable(T tabletype) throws SQLException {
        return loadTable(tabletype, null);
    }

    private static void resultRowToTable(ResultSet rs, Table table) throws SQLException {
        for (Column column : table.getColumns()) {
            String columnName = column.getColumnName();
            Class columnType = column.getType();
            if (columnType == String.class) {
                table.set(column, rs.getString(columnName));
            } else if (columnType == Integer.class) {
                table.set(column, rs.getInt(columnName));
            } else if (columnType == Timestamp.class) {
                table.set(column, rs.getTimestamp(columnName));
            } else if (columnType == Date.class) {
                table.set(column, rs.getDate(columnName));
            }
        }
    }
}
