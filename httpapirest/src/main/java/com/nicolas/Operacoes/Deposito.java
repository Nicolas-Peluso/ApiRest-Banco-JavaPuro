package com.nicolas.Operacoes;

import com.nicolas.Aux.verifica;
import com.nicolas.Cliente.Cliente;
import com.nicolas.Manipulacao.Inserir;
import com.nicolas.Manipulacao.Seleciona;

public class Deposito {

    public static boolean DepositoConta(String cpf, double Valor) {
        Cliente cliente = new Cliente();

        if (!verifica.VerificaCpf(cpf)) {
            return false;
        }

        // cpf do cliente deposito
        int TempIdClienteDepositoTo = Seleciona.SelecionaIdCliente(cpf);

        if (TempIdClienteDepositoTo == -1) {
            return false;
        }
        // seleciona conta do cliente para deposito
        int IdConta = Seleciona.SelecionaIdConta(TempIdClienteDepositoTo);

        if (IdConta == -1) {
            return false;
        }

        double SaldoAtual = Seleciona.SelecionaSaldo(IdConta);
        Valor += SaldoAtual;
        Inserir.DepositarDinheiro(Valor, IdConta);
        Valor = 0;
        if (cpf.equals(cliente.getCpf())) {
            // se deposito na propria conta atualiza o saldo
            Saldo.ConsultaSaldo();
        }
        return true;
    }
}
