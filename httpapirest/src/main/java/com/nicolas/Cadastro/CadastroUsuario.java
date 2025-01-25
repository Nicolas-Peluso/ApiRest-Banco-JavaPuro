package com.nicolas.Cadastro;

import com.nicolas.Jwt.jwt;
import com.nicolas.MD5.Md5;
import com.nicolas.Manipulacao.Inserir;

public class CadastroUsuario {
    public static String CadastrarUsuario(String nome, String senha, String cpf){
        String tempMd5 = Md5.EncriptaMd5(senha);
        String tokenCadastro = jwt.GeraToken(cpf);

        boolean CadsatroSucesso = Inserir.InserirCliente(nome, cpf, tempMd5, tokenCadastro);

        if(CadsatroSucesso){
            return tokenCadastro;
        } 
        return "";
    }
}
