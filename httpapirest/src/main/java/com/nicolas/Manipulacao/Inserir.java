package com.nicolas.Manipulacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.nicolas.Cliente.Cliente;
import com.nicolas.ConectDb.Conection;

public class Inserir {
    private static Cliente client = new Cliente();

    private static void InserirConta(){
        int idCliente = Seleciona.SelecionaIdCliente(client.getCpf());
        client.setIdCliente(idCliente);
        try{
            Connection c = Conection.ConectToDb();
            PreparedStatement stm = c.prepareStatement("INSERT INTO Conta(IdCliente) VALUES(?)");
            stm.setInt(1, client.getIdCliente());
            stm.executeUpdate();
            System.out.println("Conta Criada");
        } catch(SQLException e){
            System.out.println(e);
        }
    }

    public static boolean InserirCliente(){
        
        try{
            Connection c = Conection.ConectToDb();
            PreparedStatement stm = c.prepareStatement("INSERT INTO Cliente(nome, cpf, senha, token) VALUES(?, ?, ?, ?)");
            stm.setString(1, client.getNome());
            stm.setString(2, client.getCpf());
            stm.setString(3, client.getSenha());
            stm.setString(4, client.getTokenSession());
            stm.executeUpdate();
            System.out.println("Cliente Cadastrado com sucesso");
            InserirConta();
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

    public static void DepositarDinheiro(double valor, int Conta){
        try{
            Connection c = Conection.ConectToDb();
            PreparedStatement stm = c.prepareStatement("UPDATE Conta Set saldo = ? WHERE IdCOnta = ?;");
            stm.setDouble(1, valor);
            stm.setInt(2, Conta);
            stm.executeUpdate();
            System.err.println("Deposito Realizado Com sucesso");
        } catch(SQLException e){
            System.out.println(e);
        }
    }   
}
