package com.nicolas.ConectDb;

import java.sql.Connection;
import java.sql.SQLException;

import com.nicolas.Jwt.propriets;

import java.sql.DriverManager;

public class Conection {
    public static Connection ConectToDb() {
        Connection c = null;
        try {
            c = DriverManager.getConnection(propriets.URL_DB);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return c;
    }
}
