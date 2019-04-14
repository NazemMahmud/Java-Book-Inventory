package bookinventory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author nazem
 */
public class DBConnection {
    public static Connection getConnection() throws SQLException{
        String dbName="bookinventory";
        String userName="root";
        String password="";

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/"+dbName,userName,password);
        
        return conn;
    }
}
