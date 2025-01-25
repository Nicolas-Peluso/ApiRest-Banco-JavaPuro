package com.nicolas.Manipulacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.nicolas.ConectDb.Conection;

public class Inserir {

    private static void InserirConta(String cpf){
        int idCliente = Seleciona.SelecionaIdCliente(cpf);
        try{    
            Connection c = Conection.ConectToDb();
            PreparedStatement stm = c.prepareStatement("INSERT INTO Conta(IdCliente) VALUES(?)");
            stm.setInt(1, idCliente);
            stm.executeUpdate();
            System.out.println("Conta Criada");
        } catch(SQLException e){
            System.out.println(e);
        }
    }

    public static boolean InserirCliente(String nome, String cpf, String senha, String token){
        
        try{
            Connection c = Conection.ConectToDb();
            PreparedStatement stm = c.prepareStatement("INSERT INTO Cliente(nome, cpf, senha, token) VALUES(?, ?, ?, ?)");
            stm.setString(1, nome);
            stm.setString(2, cpf);
            stm.setString(3, senha);
            stm.setString(4, token);
            stm.executeUpdate();
            System.out.println("Cliente Cadastrado com sucesso");
            InserirConta(cpf);
            return true;
        }catch(SQLException e){
            System.out.println(e);
        }
        return false;
    }

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
