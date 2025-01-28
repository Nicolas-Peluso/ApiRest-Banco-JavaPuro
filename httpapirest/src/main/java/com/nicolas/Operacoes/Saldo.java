package com.nicolas.Operacoes;

import com.nicolas.Cliente.Cliente;
import com.nicolas.Manipulacao.Seleciona;

public class Saldo {

    public static boolean ConsultaSaldo() {
        Cliente cliente = new Cliente();

        double saldo = Seleciona.SelecionaSaldo(cliente.getConta().getIdconta());
        cliente.getConta().setSaldo(saldo);

        return true;
    }
}
