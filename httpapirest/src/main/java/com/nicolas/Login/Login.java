package com.nicolas.Login;

import com.nicolas.Aux.verifica;
import com.nicolas.Cliente.Cliente;
import com.nicolas.Cliente.Conta;
import com.nicolas.Jwt.jwt;
import com.nicolas.Manipulacao.Inserir;
import com.nicolas.Manipulacao.Seleciona;

public class Login {

    public static String LoginVerifica() {
        Cliente cliente = new Cliente();
        Conta conta = new Conta();

        boolean CpfEstruturaValida = verifica.VerificaCpf(""); // Verifica Estrutura Do Cpf
        if (!CpfEstruturaValida) {
            return "cpf";
        }

        String SenhaDb = Seleciona.SelectSenha(cliente.getCpf()); // Busca Senha no banco referente ao cpf se nao
                                                                  // retornar uma senha CPF Invalido

        if (SenhaDb.isEmpty()) {
            return "";
        }
        // Valida senha e chaca se token no banco é valido se nao gera um novo
        if (SenhaDb.equals(cliente.getSenha())) {
            String tk = Seleciona.SelectJWT(cliente.getCpf());
            boolean ValidToken = jwt.ValidaToken(tk);
            int id = Seleciona.SelecionaIdCliente(cliente.getCpf());
            cliente.setIdCliente(id);
            if (!ValidToken) {
                cliente.setTokenSession(jwt.GeraToken(cliente.getCpf()));
                Inserir.InserirToken(cliente.getCpf(), cliente.getTokenSession());
                return cliente.getTokenSession();
            }
            cliente.setTokenSession(tk);

            int idConta = Seleciona.SelecionaIdConta(cliente.getIdCliente());
            double saldoAtual = Seleciona.SelecionaSaldo(idConta);

            conta.setIdconta(idConta);
            conta.setSaldo(saldoAtual);

            cliente.setConta(conta);
            return "Login Realizado Com Sucesso.";
        } else {
            return "SENHA";
        }
    }
}
