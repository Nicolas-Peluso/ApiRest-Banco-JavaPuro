package com.nicolas.Operacoes;

import com.nicolas.Manipulacao.Delete;
import com.nicolas.Cliente.Cliente;

public class DeletarConta {
    public static boolean Deletar(){
        Cliente cliente = new Cliente();
        double valor = Saldo.ConsultaSaldo();
        if(valor > 0){
            return false;
        }

        Delete.DeletarAConta(cliente.getIdCliente());
        SairDaConta.sair();
        return true;
    }
}
