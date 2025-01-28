package com.nicolas.Cadastro;

import com.nicolas.Cliente.Cliente;
import com.nicolas.Cliente.Conta;
import com.nicolas.Jwt.jwt;
import com.nicolas.Manipulacao.Inserir;
import com.nicolas.Manipulacao.Seleciona;

public class CadastroUsuario {
    public static void CadastrarUsuario() {
        Cliente cliente = new Cliente();
        Conta contaCliente = new Conta();

        cliente.setTokenSession(jwt.GeraToken(cliente.getCpf()));

        Inserir.InserirCliente();

        int id = Seleciona.SelecionaIdCliente(cliente.getCpf());
        cliente.setIdCliente(id);

        // Criando A conta e setando A conta Ao cliente no sistema;
        Inserir.InserirConta();
        int idConta = Seleciona.SelecionaIdConta(id);
        contaCliente.setIdconta(idConta);
        cliente.setConta(contaCliente);

    }
}
