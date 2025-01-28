package com.nicolas.Operacoes;

import com.nicolas.Cliente.Cliente;
import com.nicolas.Manipulacao.Inserir;

public class Saque {
    public static double Sacar(double Valor) {
        Cliente cliente = new Cliente();

        if (cliente.getConta().getSaldo() < Valor) {
            return -1.0;
        }

        double NovoValorConta = cliente.getConta().getSaldo() - Valor;

        Inserir.DepositarDinheiro(NovoValorConta, cliente.getConta().getIdconta());
        cliente.getConta().setSaldo(NovoValorConta);

        return Valor;
    }
}
