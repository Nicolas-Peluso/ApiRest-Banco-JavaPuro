package com.nicolas.Login;

import com.nicolas.Aux.verifica;
import com.nicolas.Jwt.jwt;
import com.nicolas.MD5.Md5;
import com.nicolas.Manipulacao.Inserir;
import com.nicolas.Manipulacao.Seleciona;

public class Login {

    public static String LoginVerifica(String Cpf, String Senha){
        boolean CpfEstruturaValida = verifica.VerificaCpf(Cpf); // Verifica Estrutura Do Cpf
        if(!CpfEstruturaValida){
            return "cpf";
        }

        String SenhaEncriptada = Md5.EncriptaMd5(Senha);
        String SenhaDb = Seleciona.SelectSenha(Cpf); //Busca Senha no banco referente ao cpf se nao retornar uma senha CPF Invalido
        
        if(SenhaDb.isEmpty()){
            return "";
        }
        
        if(SenhaDb.equals(SenhaEncriptada)){
            String JWT = Seleciona.SelectJWT(Cpf);
            if(JWT.isEmpty()){
               String NovoToken = jwt.GeraToken(Cpf);
               Inserir.InserirToken(Cpf, NovoToken);
               return NovoToken;
            }
            else{
                String NovoTokenAtt = jwt.ValidaToken(JWT);
                if(NovoTokenAtt.isEmpty()){
                    NovoTokenAtt = jwt.GeraToken(Cpf);
                    Inserir.InserirToken(Cpf, NovoTokenAtt);
                }
                return NovoTokenAtt;
            }
        } else{
            return "SENHA";
        }
    }
}
