package com.nicolas.HttpRequests_;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.nicolas.Aux.verifica;
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

                if(!cpf.isEmpty() && !Senha.isEmpty()){
                    switch (tokenRetornaCliente) {
                        case "":
                            response = "CPF nao cadastrado no banco Tente novamente";
                            break;
                        case "SENHA":
                            response = "Senha incorreta tente novamente";
                            break;
                        case "cpf":
                            response = "Cpf Invalido Verifique, cpf Deve seguir esse padrão (000.000.000-00), e ou Cpf esta incorreto ";
                            break;
                        default:
                            break;
                    }
                }else{
                    response = "Nenhum campo deve estar vazio";
                }

                if(!response.isEmpty()){
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody(); 
                    os.write(response.getBytes());
                    os.close();
                } else{
                    exchange.sendResponseHeaders(201, tokenRetornaCliente.getBytes().length);
                    OutputStream os = exchange.getResponseBody(); 
                    os.write(tokenRetornaCliente.getBytes());
                    os.close();
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }
    
    //Cadastro de usuarios
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

                    if(((!cpf.isEmpty() && !Senha.isEmpty())) && !nome.isEmpty()){
                        String validData = verifica.VerificaDados(cpf, Senha, true);
                        switch(validData){
                            case "cpf":
                                response = "Cpf Invalido Verifique, cpf Deve seguir esse padrão (000.000.000-00) e ou Cpf esta incorreto"; //verificar se cpf é valido
                                break;
                            case "cpfExiste":
                                response = "Cpf ja Cadastrado no Sistema faça login"; //verificar se cpf existe no banco
                                break;   
                            case "senha":
                                response = "Senha Deve Ter mais de 8 digitos e nao deve ter mais de 16"; //verifca Se senha é maior que 8 e mor que 16
                            default:
                                break;
                        }
                    } else{
                        response = "Nenhum Campo pode estar vazio";
                    }

                    if(response.isEmpty()){
                        response = CadastroUsuario.CadastrarUsuario(nome, Senha, cpf);
                        exchange.sendResponseHeaders(201, response.getBytes().length); // codigo 201 para informar que foi feito a mudança
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    }
                    else{
                        exchange.sendResponseHeaders(200, response.getBytes().length); // 200 informando que deu certo a conexão mas algo deu errado na hora de inserir os dados
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    }

                }else{
                    exchange.sendResponseHeaders(405, -1);
                }
            }
    }
}   
