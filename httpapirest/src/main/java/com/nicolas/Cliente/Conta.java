package com.nicolas.Cliente;

public class Conta {
    private static int Idconta;
    private static double saldo;

    public int getIdconta() {
        return Idconta;
    }

    public void setIdconta(int idconta_) {
        Idconta = idconta_;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo_) {
        saldo = saldo_;
    }

}
