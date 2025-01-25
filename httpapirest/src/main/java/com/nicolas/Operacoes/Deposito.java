package com.nicolas.Operacoes;

import com.nicolas.Aux.verifica;
import com.nicolas.Manipulacao.Inserir;
import com.nicolas.Manipulacao.Seleciona;

public class Deposito {

    public static boolean DepositoConta(String cpf, double Valor){
        if(!verifica.VerificaCpf(cpf)){
            return false;
        }
        
        int TempIdClienteDepositoTo = Seleciona.SelecionaIdCliente(cpf);
        
        if(TempIdClienteDepositoTo == -1){
            return false;
        }

        int IdConta = Seleciona.SelecionaIdConta(TempIdClienteDepositoTo);

        if(IdConta == -1){
            return false;
        }

        double SaldoAtual = Seleciona.SelecionaSaldo(IdConta);
        Valor += SaldoAtual;
        Inserir.DepositarDinheiro(Valor, IdConta);
        Valor = 0;
        return true;
    }
}
