package action;

import java.sql.*;

// This Class handles connection to the Database
// Each instance get a new connection and have to close at the end of the function

public class ConnectDB {
    public Statement stmt;
    public PreparedStatement preparedStmt;

    public    Connection conn;
    private   String url = "jdbc:sqlserver://localhost:1433;databaseName=FoodDelivery;user=sa;password=quoctuan123;encrypt=false";

    public ConnectDB() throws SQLException {
        conn = DriverManager.getConnection(url);
        stmt = conn.createStatement();
    }



    public void closeConnection() throws SQLException {
        conn.close();
    }

    public Connection getConn() {
        return conn;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
