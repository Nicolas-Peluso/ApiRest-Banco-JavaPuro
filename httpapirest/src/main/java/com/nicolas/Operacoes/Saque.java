package com.nicolas.Operacoes;

import com.nicolas.Cliente.Cliente;
import com.nicolas.Manipulacao.Inserir;
import com.nicolas.Manipulacao.Seleciona;

public class Saque {
    public static double Sacar(double Valor){
        Cliente cliente = new Cliente();
        double SaldoAtual = Saldo.ConsultaSaldo();
        if(SaldoAtual == -1.0){
            return -1.0;
        }
        
        if(SaldoAtual < Valor){
            return -2.0;
        }

        double NovoValorConta = SaldoAtual - Valor;
        int idConta = Seleciona.SelecionaIdConta(cliente.getIdCliente());
        Inserir.DepositarDinheiro(NovoValorConta, idConta);
        
        return Valor;
    }
}
