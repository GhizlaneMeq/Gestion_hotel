package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private static Connection connection;
    private String url ="jdbc:postgresql://localhost:5432/postgres";
    private String user = "Gestion_hotel";
    private String password = "";
    private   DatabaseConnection(){
        try {
            connection = DriverManager.getConnection(url , user , password);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public  static DatabaseConnection getInstance(){
        if(instance == null){
            instance = new DatabaseConnection();
        }
        return instance;

    }
    public Connection getConnection(){
        return connection;
    }
}