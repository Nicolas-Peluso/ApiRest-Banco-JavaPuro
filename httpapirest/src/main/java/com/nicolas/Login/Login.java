package com.nicolas.Login;

import com.nicolas.Aux.verifica;
import com.nicolas.Cliente.Cliente;
import com.nicolas.Jwt.jwt;
import com.nicolas.Manipulacao.Inserir;
import com.nicolas.Manipulacao.Seleciona;

public class Login {

    public static String LoginVerifica(){
        Cliente cliente = new Cliente();
        boolean CpfEstruturaValida = verifica.VerificaCpf(""); // Verifica Estrutura Do Cpf
        if(!CpfEstruturaValida){
            return "cpf";
        }

        String SenhaDb = Seleciona.SelectSenha(cliente.getCpf()); //Busca Senha no banco referente ao cpf se nao retornar uma senha CPF Invalido
        
        if(SenhaDb.isEmpty()){
            return "";
        }
        
        if(SenhaDb.equals(cliente.getSenha())){
            String tk = Seleciona.SelectJWT(cliente.getCpf());
            boolean ValidToken = jwt.ValidaToken(tk);
            int id = Seleciona.SelecionaIdCliente(cliente.getCpf());
            cliente.setIdCliente(id);
            if(!ValidToken){
                cliente.setTokenSession(jwt.GeraToken(cliente.getCpf()));
                Inserir.InserirToken(cliente.getCpf(), cliente.getTokenSession());
                return cliente.getTokenSession();
            }
            cliente.setTokenSession(tk);
            return cliente.getTokenSession();
            }    
            else{
            return "SENHA";
        }
        } 
    }
