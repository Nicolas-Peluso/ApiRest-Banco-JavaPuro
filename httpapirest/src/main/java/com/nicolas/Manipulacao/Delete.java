package com.nicolas.Manipulacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.nicolas.ConectDb.Conection;

public class Delete {
    public static void DeletarAConta(int Id){
        try{
            Connection c = Conection.ConectToDb();
            PreparedStatement stm = c.prepareStatement("DELETE FROM Cliente WHERE IdCliente = ?;");
            stm.setInt(1, Id);
            stm.executeUpdate();
        } catch(SQLException e){
            System.out.println(e);
        }
    }
}
