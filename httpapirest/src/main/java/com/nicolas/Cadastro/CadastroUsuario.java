package com.nicolas.Cadastro;

import com.nicolas.Aux.verifica;

public class CadastroUsuario {

    public static boolean VerificaDados(String _cpf, String nome, String senha){
        boolean Cpfvalido = verifica.VerificaCpf(_cpf);
        if(Cpfvalido){
            return true;
        }
        //verificar Senha
        //Verificar Nome
        return false;
    }
}
