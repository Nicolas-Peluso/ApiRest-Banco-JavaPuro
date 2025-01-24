package com.nicolas.Aux;
import java.util.ArrayList;

public class verifica {

    public static boolean VerificaCpf(String cpf){
        //Validação de CPF Matematicamanete;
        //REFATORAR
        char[] cpfChar = cpf.toCharArray();
        int Digverifcador1 = 0;
        int Digverifcador2 = 0;
        ArrayList<Integer> cpfInt = new ArrayList<>();

        for(int i = 0; i < cpfChar.length; i++){
            if(cpfChar[i] != '.' && cpfChar[i] != '-'){
                cpfInt.add(Character.getNumericValue(cpfChar[i]));
            }
            if(cpfChar[i] == '-'){
                Digverifcador1 = Character.getNumericValue(cpfChar[i + 1]);
                Digverifcador2 = Character.getNumericValue(cpfChar[i + 2]);
                break;
            }
        }

        int cont = 10;
        int Result1 = 0;
        
        for(int e : cpfInt){
            Result1 += (e * cont);
            cont--;
        }

        if(11 - (Result1 % 11) >= 10){
            if(!(Digverifcador1 == 0)){
                return false;
            }
        }else if(!(11 - (Result1 % 11) == Digverifcador1)){
            return false;
        }

        cont = 11;
        cpfInt.add(Digverifcador1);
        Result1 = 0;
        for(int e : cpfInt){
            Result1 += (e * cont);
            cont--;
        }

        if(11 - (Result1 % 11) >= 10){
            if(!(Digverifcador2 == 0)){
                return false;
            }
        }else if(!(11 - (Result1 % 11) == Digverifcador2)){
            return false;
        }
        
        return true;
    }
}
