package com.nicolas.Operacoes;
import com.nicolas.Cliente.Cliente;

public class SairDaConta {
    public static void sair(){
        Cliente cliente = new Cliente();
        cliente.setCpf(null);
        cliente.setSenha(null);
        cliente.setTokenSession(null);
        cliente.setNome(null);
        cliente.setIdCliente(-1);
    }
}
