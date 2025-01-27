package com.nicolas.HttpRequests_;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.nicolas.Aux.EstaLogado;
import com.nicolas.Aux.verifica;
import com.nicolas.Cadastro.CadastroUsuario;
import com.nicolas.Cliente.Cliente;
import com.nicolas.Jwt.jwt;
import com.nicolas.Login.Login;
import com.nicolas.MD5.Md5;
import com.nicolas.Operacoes.Saldo;
import com.nicolas.Operacoes.Saque;
import com.nicolas.Operacoes.Deposito;
import com.nicolas.Operacoes.SairDaConta;
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

class DespotioRequest {
    private String cpf;
    private double valor;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

}

class SacarReq {
    private double valor;

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}

public class HttpReq {

    public void StartServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/login", new MiddleWareSetCors(new LoginPost()));
        server.createContext("/register", new MiddleWareSetCors(new Cadastro()));
        server.createContext("/deposit", new MiddleWareSetCors(new Despositar()));
        server.createContext("/saldo", new MiddleWareSetCors(new PegaSaldo()));
        server.createContext("/sacar", new MiddleWareSetCors(new SacarDinheiro()));
        server.createContext("/sair", new MiddleWareSetCors(new SairConta()));
        server.createContext("/deletar", new MiddleWareSetCors(new DeletarConta()));

        System.out.println("Servidor iniciado na porta 8080");
        server.start();
    }

    class LoginPost implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            Cliente cliente = new Cliente();

            if ("POST".equals(exchange.getRequestMethod())) {
                Gson gson = new Gson();
                InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                LoginRequest UserData = gson.fromJson(reader, LoginRequest.class);

                cliente.setCpf(UserData.getCpf());
                cliente.setSenha(Md5.EncriptaMd5(UserData.getPassword()));

                String response = "";

                String tokenRetornaCliente = Login.LoginVerifica();

                if (!cliente.getCpf().isEmpty() && !cliente.getSenha().isEmpty()) {
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
                } else {
                    response = "Nenhum campo deve estar vazio";
                }

                if (!response.isEmpty()) {
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } else {
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

    // Cadastro de usuarios
    public class Cadastro implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            Cliente cliente = new Cliente();

            if ("POST".equals(exchange.getRequestMethod())) {

                Gson gson = new Gson();
                InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                RegistroRequest UserData = gson.fromJson(reader, RegistroRequest.class);

                cliente.setCpf(UserData.getCpf());
                cliente.setSenha(UserData.getPassword());
                cliente.setNome(UserData.getNome());

                String response = "";

                if (((!cliente.getCpf().isEmpty() && !cliente.getSenha().isEmpty())) && !cliente.getNome().isEmpty()) {
                    String validData = verifica.VerificaDados(true);
                    switch (validData) {
                        case "cpf":
                            response = "Cpf Invalido Verifique, cpf Deve seguir esse padrão (000.000.000-00) e ou Cpf esta incorreto"; // verificar
                                                                                                                                       // se
                                                                                                                                       // cpf
                                                                                                                                       // é
                                                                                                                                       // valido
                            break;
                        case "cpfExiste":
                            response = "Cpf ja Cadastrado no Sistema faça login"; // verificar se cpf existe no banco
                            break;
                        case "senha":
                            response = "Senha Deve Ter mais de 8 digitos e nao deve ter mais de 16"; // verifca Se senha
                                                                                                     // é maior que 8 e
                                                                                                     // mor que 16
                        default:
                            break;
                    }
                } else {
                    response = "Nenhum Campo pode estar vazio";
                }

                if (response.isEmpty()) {
                    CadastroUsuario.CadastrarUsuario();
                    response = cliente.getTokenSession();
                    exchange.sendResponseHeaders(201, response.getBytes().length); // codigo 201 para informar que foi
                                                                                   // feito a mudança
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } else {
                    exchange.sendResponseHeaders(200, response.getBytes().length); // 200 informando que deu certo a
                                                                                   // conexão mas algo deu errado na
                                                                                   // hora de inserir os dados
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }

            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    // Despoitar
    public class Despositar implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            Cliente cliente = new Cliente();

            if ("POST".equals(exchange.getRequestMethod())) {

                Gson gson = new Gson();
                InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                DespotioRequest UserData = gson.fromJson(reader, DespotioRequest.class);

                String cpf = UserData.getCpf();
                double valor = UserData.getValor();
                String response = "";
                boolean logado = false;

                if (!(valor == 0.0) && !cpf.isEmpty()) {
                    logado = EstaLogado.Logado();
                    if (!logado) {
                        response = "é necessario estar logado em uma conta para realizar essa operação";
                    }

                    while (logado) {
                        if (!jwt.ValidaToken(cliente.getTokenSession())) {
                            response = "Token De Sessão invalido Faça login novamente";
                            break;
                        }
                        boolean DespitoFeito = Deposito.DepositoConta(cpf, valor);
                        if (!DespitoFeito) {
                            response = "Algo Deu Errado na operação de deposito Verifique os dados";
                            break;
                        }
                        response = "";
                        break;
                    }
                } else {
                    response = "cpf nao pode ser vazio e/ou Valor deve ser maior que 0";
                }

                if (response.isEmpty()) {
                    response = "Deposito Realizado com sucesso";
                    exchange.sendResponseHeaders(201, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } else {
                    exchange.sendResponseHeaders(301, response.getBytes().length); // Nao permitido
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            }
        }
    }

    public class PegaSaldo implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            Cliente cliente = new Cliente();
            if ("GET".equals(exchange.getRequestMethod())) {
                String response = "";
                double Saldores = 0.0;
                boolean logado = EstaLogado.Logado();
                if (!logado) {
                    response = "Voce deve estar logado para realizar consultas";
                }

                if (logado) {
                    boolean TokenValido = false;
                    do {
                        if (!(jwt.ValidaToken(cliente.getTokenSession()))) {
                            response = "token Exprirado faça login novamente";
                            break;
                        }
                        Saldores = Saldo.ConsultaSaldo();
                        break;

                    } while (TokenValido);
                }
                if (Saldores == -1.0) {
                    response = "Tente novamente algo deu erra na manipulação dos dados";
                }
                if (response.isEmpty()) {
                    response = Double.toString(Saldores);
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } else {
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            }
        }
    }

    public class SacarDinheiro implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {

            Cliente cliente = new Cliente();
            if ("POST".equals(exchange.getRequestMethod())) {
                Gson gson = new Gson();
                InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                SacarReq UserData = gson.fromJson(reader, SacarReq.class);

                double valor = UserData.getValor();

                String response = "";
                double SaqueMon = 0.0;
                boolean logado = EstaLogado.Logado();
                if (!logado) {
                    response = "Voce deve estar logado para realizar consultas";
                }

                if (logado) {
                    boolean TokenValido = false;
                    do {
                        if (!(jwt.ValidaToken(cliente.getTokenSession()))) {
                            response = "token Exprirado faça login novamente";
                            break;
                        }
                        SaqueMon = Saque.Sacar(valor);
                        break;

                    } while (TokenValido);
                }
                if (SaqueMon == -2.0) {
                    response = "Saldo insuficiente Para Relizar um saque";
                }

                if (SaqueMon == -1.0) {
                    response = "Algo deu errado durante a operação tente novamente";
                }

                if (response.isEmpty()) {
                    response = Double.toString(SaqueMon);
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } else {
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            }
        }
    }

    public class SairConta implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                String response = "";
                boolean logado = EstaLogado.Logado();
                if (!logado) {
                    response = "Voce deve estar logado para realizar consultas";
                }

                if (logado) {
                    SairDaConta.sair();
                }

                if (response.isEmpty()) {
                    response = "Adeus";
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } else {
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            }
        }
    }

    public class DeletarConta implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            Cliente cliente = new Cliente();
            if ("GET".equals(exchange.getRequestMethod())) {
                String response = "";
                boolean logado = EstaLogado.Logado();
                if (!logado) {
                    response = "Voce deve estar logado para realizar consultas";
                }

                while (logado) {
                    if (jwt.ValidaToken(cliente.getTokenSession())) {
                        boolean TemSaldo = com.nicolas.Operacoes.DeletarConta.Deletar();
                        System.err.println(TemSaldo);
                        if (!TemSaldo) {
                            response = "Nao é possivel deletar uma conta que possui saldo nela";
                        }
                        break;
                    } else {
                        response = "Token invalido faca login novamete";
                    }
                    break;
                }

                if (response.isEmpty()) {
                    response = "Conta Deletada Com sucesso";
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } else {
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            }
        }
    }
}
