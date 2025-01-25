package com.nicolas.Operacoes;

import com.nicolas.Cliente.Cliente;
import com.nicolas.Manipulacao.Seleciona;

public class Saldo {

    public static double ConsultaSaldo(){
        Cliente cliente = new Cliente();

        int id = Seleciona.SelecionaIdConta(cliente.getIdCliente());

        double saldo = Seleciona.SelecionaSaldo(id);

        return saldo;
    }
}
