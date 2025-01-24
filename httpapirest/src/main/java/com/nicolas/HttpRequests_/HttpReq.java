package com.nicolas.HttpRequests_;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.nicolas.Cadastro.CadastroUsuario;
import com.nicolas.Login.Login;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.google.gson.Gson;
import java.io.InputStreamReader;

class LoginRequest {
    private String cpf;
    private String password;
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}

class RegistroRequest {
    private String cpf;
    private String password;
    private String nome;
    
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}

public class HttpReq {

    public void StartServer() throws IOException{
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        server.createContext("/login", new LoginPost());
        server.createContext("/register", new Cadastro());

 
        System.out.println("Servidor iniciado na porta 8080");
        server.start();
    }

    class LoginPost implements HttpHandler {
        
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {

                Gson gson = new Gson();
                InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                LoginRequest UserData = gson.fromJson(reader, LoginRequest.class);

                String cpf = UserData.getCpf();
                String Senha = UserData.getPassword();

                String response = "";

                String tokenRetornaCliente = Login.LoginVerifica(cpf, Senha);

                if(tokenRetornaCliente.isEmpty()){
                    response = "CPF INVALIDO";
                }

                if(tokenRetornaCliente.equals("SENHA")){
                    response = "Senha incorreta tente novamente";
                }

                if(!response.isEmpty()){
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody(); 
                    os.write(response.getBytes());
                    os.close();
                } else{
                    exchange.sendResponseHeaders(200, tokenRetornaCliente.getBytes().length);
                    OutputStream os = exchange.getResponseBody(); 
                    os.write(tokenRetornaCliente.getBytes());
                    os.close();
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }
    
    public class Cadastro implements HttpHandler{
            public void handle(HttpExchange exchange) throws IOException{
                if("POST".equals(exchange.getRequestMethod())){

                    Gson gson = new Gson();
                    InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                    RegistroRequest UserData = gson.fromJson(reader, RegistroRequest.class);

                    String cpf = UserData.getCpf();
                    String Senha = UserData.getPassword();
                    String nome = UserData.getNome();
                    String response = "";

                    if((!cpf.isEmpty() || !Senha.isEmpty()) || !nome.isEmpty()){
                        boolean validData = CadastroUsuario.VerificaDados(cpf, nome, Senha);
                        if(validData){
                            System.out.println("Cpf Valido"); //Validar cpf // Validar Nome // Validar Senha // Tudo certo Cadastrar Usuario no banco e Criar uma conta vinculado ao Id
                        } else{
                            System.out.println("Invalido");
                        }
                    } 
                    else{
                        response = "Nenhum Campo pode estar vazio";
                    }

                }else{
                    exchange.sendResponseHeaders(405, -1);
                }
            }
    }
}   
