package com.nicolas.Manipulacao;

import java.sql.SQLException;

import com.nicolas.ConectDb.Conection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class Seleciona {
    
    public static String SelectNome(){
        try{
            Connection c = Conection.ConectToDb();
            PreparedStatement stm = c.prepareStatement("SELECT nome FROM Cliente WHERE cpf=?");
            stm.setString(1, "453927394-23");
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                return rs.getString("nome");
            }
        } catch(SQLException e){
            System.out.println(e);
        }
        return "";
    }

    public static String SelectCpfESenha(String cpf){
        try{
            Connection c = Conection.ConectToDb();
            PreparedStatement stm = c.prepareStatement("SELECT cpf, senha FROM Cliente WHERE cpf=?");
            stm.setString(1, cpf);
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                return rs.getString("senha");
            }
        } catch(SQLException e){
            System.out.println(e);
        }
        return "";
    }

    public static String SelectJWT(String cpf){
        try{
            Connection c = Conection.ConectToDb();
            PreparedStatement stm = c.prepareStatement("SELECT token FROM Cliente WHERE cpf=?");
            stm.setString(1, cpf);
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                return rs.getString("token");
            }
        } catch(SQLException e){
            System.out.println(e);
        }
        return "";
    }
}
