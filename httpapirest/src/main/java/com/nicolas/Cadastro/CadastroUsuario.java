package com.nicolas.Cadastro;

import com.nicolas.Cliente.Cliente;
import com.nicolas.Jwt.jwt;
import com.nicolas.Manipulacao.Inserir;
import com.nicolas.Manipulacao.Seleciona;

public class CadastroUsuario {
    public static void CadastrarUsuario(){
        Cliente cliente = new Cliente();

        cliente.setTokenSession(jwt.GeraToken(cliente.getCpf()));

        boolean CadsatroSucesso = Inserir.InserirCliente();

        if(CadsatroSucesso){
            int id = Seleciona.SelecionaIdCliente(cliente.getCpf());
            cliente.setIdCliente(id);
        }
    }
}
