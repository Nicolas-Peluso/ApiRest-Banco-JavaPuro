package com.nicolas.Cliente;

public class Cliente {
    private static String nome; //deve ser static para outros contextos terem acesso.
    private static String senha;
    private static String cpf;
    private static int IdCliente;
    private static String tokenSession;

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
