package io.github.toberocat.core.utility.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQL {

    public enum Value {
        VARCHAR(100),INT(100);

        private int value;

        Value(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static class Column {
        private Value value;
        private String columnName;

        public Column(Value value, String columnName) {
            this.value = value;
            this.columnName = columnName;
        }

        public Value getValue() {
            return value;
        }

        public void setValue(Value value) {
            this.value = value;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }
    }

    public class SQLTable {
        private final String tableName;
        private final Connection connection;
        private Column[] columns;

        public SQLTable(String tableName, Connection connection) {
            this.tableName = tableName;
            this.connection = connection;
        }

        public SQLTable setColumns(Column... columns) {
            this.columns = columns;
            return this;
        }

        public <T> SQLTable addRow(T... columnValues) throws SQLValueError {
            if (columnValues.length != columns.length){
                throw new SQLValueError("Expecting " + columns.length + ". Received " + columnValues.length);
            }
            try {
                PreparedStatement ps = connection.prepareStatement("INSERT IGNORE INTO " + tableName +
                        " "+columnsToRowA(columns)+" VALUES " + columnsToRowA("?", columns));
                for (int i = 1; i <= columnValues.length; i++) {
                    ps.setObject(i, columnValues[i]);
                }

                ps.executeUpdate();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            return this;
        }

        private <T> boolean exists(Column column, T value) {
            try {
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM "+tableName+" WHERE "+column.columnName+"=?");
                ps.setString(1, value.toString());
                ResultSet result = ps.executeQuery();
                return result.next();
            } catch (SQLException exception) {
                exception.printStackTrace();
                return false;
            }
        }
    }

    private Connection connection;

    public SQL(Connection connection) {
        this.connection = connection;
    }

    public SQLTable select(String table) {
        return new SQLTable(table, connection);
    }

    public SQLTable createTable(String table, Column... columns) {
        try {
            PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS "+table+" "+columnsToString(columns));
            ps.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new SQLTable(table, connection).setColumns(columns);
    }

    private String columnsToString(Column[] columns) {
        String output = "(";
        for (Column column : columns) {
            output += column.getColumnName() + " " + column.getValue().toString() + "(" + column.getValue().getValue() + ")" + ",";
        }

        output += "PRIMARY KEY ("+columns[0].getValue().toString()+")";
        output += ")";

        return output;
    }

    private String columnsToRowA(Column[] columns) {
        String output = "(";

        for (int i = 0; i < columns.length; i++) {
            output += columns[i].getColumnName() + (i != columns.length - 1 ? ",": "");
        }
        return output + ")";
    }

    private String columnsToRowA(String fill, Column[] columns) {
        String output = "(";

        for (int i = 0; i < columns.length; i++) {
            output += fill+ (i != columns.length - 1 ? ",": "");
        }
        return output + ")";
    }
}
