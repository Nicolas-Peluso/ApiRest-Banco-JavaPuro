package com.nicolas.Manipulacao;

import java.sql.SQLException;

import com.nicolas.ConectDb.Conection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Seleciona {

    public static int SelecionaIdConta(int IdCliente){
        try{
            Connection c = Conection.ConectToDb();
            PreparedStatement stm = c.prepareStatement("SELECT IdConta FROM Conta WHERE IdCliente = ?");
            stm.setInt(1, IdCliente);
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                return rs.getInt("IdConta");
            }

        }catch(SQLException e){
            System.out.println(e);
        }
        return -1;
    }

    public static double SelecionaSaldo(int IdConta){
        try{
            Connection c = Conection.ConectToDb();
            PreparedStatement stm = c.prepareStatement("SELECT saldo FROM Conta WHERE IdConta = ?;");
            stm.setInt(1, IdConta);
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                return rs.getDouble("saldo");
            }
        }catch(SQLException e){ 
            System.out.println(e);
        }
        return -1.0;
    }

    public static int SelecionaIdCliente(String cpf){
        try{
            Connection c = Conection.ConectToDb();
            PreparedStatement stm = c.prepareStatement("SELECT IdCliente FROM Cliente WHERE cpf = ?");
            stm.setString(1, cpf);
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                return rs.getInt("IdCliente");
            }
        }catch(SQLException e){
            System.out.println(e);
        }
        return -1;
    }
    
    public static String SelectCpf(String cpf){
        try{
            Connection c = Conection.ConectToDb();
            PreparedStatement stm = c.prepareStatement("SELECT cpf FROM Cliente WHERE cpf=?");
            stm.setString(1, cpf);
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                return rs.getString("cpf");
            }
        } catch(SQLException e){
            System.out.println(e);
        }
        return "";
    }

    public static String SelectSenha(String cpf){
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
