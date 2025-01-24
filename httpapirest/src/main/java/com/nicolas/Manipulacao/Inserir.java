package com.nicolas.Manipulacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.nicolas.ConectDb.Conection;

public class Inserir {

    public static void InserirToken(String cpf, String novoJwt){
        try{
            Connection c = Conection.ConectToDb();
            PreparedStatement stm = c.prepareStatement("UPDATE Cliente SET token = ? WHERE cpf = ?;");
            stm.setString(1, novoJwt);
            stm.setString(2, cpf);
            stm.executeUpdate();
            System.out.println("Token Adicionado com sucesso");
        }catch(SQLException e){
            System.out.println(e);
        }
    }
}
