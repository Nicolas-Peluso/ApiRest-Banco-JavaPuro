package com.nicolas.ConectDb;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class Conection {
    public Connection ConectToDb(){
        String url = "jdbc:mysql://localhost:3306/bancoConta?user=root&password=123456789";
        Connection c = null;
        try{
            c = DriverManager.getConnection(url);
        } catch(SQLException e){    
            System.out.println(e);
        }
        return c;
    }
}
