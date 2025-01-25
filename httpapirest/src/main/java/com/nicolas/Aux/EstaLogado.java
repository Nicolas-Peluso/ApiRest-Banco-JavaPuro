package com.nicolas.Aux;

import com.nicolas.Cliente.Cliente;

public class EstaLogado {

    public static boolean Logado(){
        Cliente cliente = new Cliente();

        if(cliente.getTokenSession() == null){
            return false;
        }
        
        return true;
    }

}
