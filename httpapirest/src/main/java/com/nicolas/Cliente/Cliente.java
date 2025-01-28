package com.nicolas.Cliente;

public class Cliente {
    private static String nome; // deve ser static para outros contextos terem acesso.
    private static String senha;
    private static String cpf;
    private static int IdCliente;
    private static String tokenSession;
    private static Conta conta; // cliente possui uma conta relacionamento de classe 1 -> 1;

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta_) {
        conta = conta_;
    }

    public int getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(int _idCliente) {
        IdCliente = _idCliente;
    }

    public String getTokenSession() {
        return tokenSession;
    }

    public void setTokenSession(String _tokenSession) {
        tokenSession = _tokenSession;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String _nome) {
        nome = _nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String _senha) {
        senha = _senha;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String _cpf) {
        cpf = _cpf;
    }
}
