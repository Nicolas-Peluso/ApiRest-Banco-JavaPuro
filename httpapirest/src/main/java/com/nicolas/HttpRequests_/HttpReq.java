package com.nicolas.HttpRequests_;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.nicolas.Aux.Erro;
import com.nicolas.Aux.EstaLogado;
import com.nicolas.Aux.PassFine;
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
                
                Erro erro = new Erro();
                PassFine rs = new PassFine();

                cliente.setCpf(UserData.getCpf());

                if (!cliente.getCpf().isEmpty() && !UserData.getPassword().isEmpty()) {
                    //Encripta somente depois de verificar se a senha "pura" nao esta vazia
                    cliente.setSenha(Md5.EncriptaMd5(UserData.getPassword()));

                    String tokenRetornaCliente = Login.LoginVerifica();
                    switch (tokenRetornaCliente) {
                        case "":
                            erro.setResponse("CPF nao cadastrado no banco Tente novamente");
                            break;
                        case "SENHA":
                            erro.setResponse("Senha incorreta tente novamente");
                            break;
                        case "cpf":
                            erro.setResponse(
                                    "Cpf Invalido Verifique, cpf Deve seguir esse padrão (000.000.000-00), e ou Cpf esta incorreto");
                            break;
                        default:
                            break;
                    }
                } else {
                    erro.setResponse("Nenhum campo deve estar vazio");
                }
                if (!erro.getResponse().isEmpty()) {
                    String response = gson.toJson(erro);
                    exchange.getResponseHeaders().set("Content-type", "application/json");
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } else {
                    rs.setMessage("Login Realizado Com sucesso");
                    String res = gson.toJson(rs);
                    exchange.getResponseHeaders().set("Content-type", "application/json");
                    exchange.sendResponseHeaders(201, res.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(res.getBytes());
                    os.close();
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    public class Cadastro implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            Cliente cliente = new Cliente();

            if ("POST".equals(exchange.getRequestMethod())) {

                Gson gson = new Gson();
                InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                RegistroRequest UserData = gson.fromJson(reader, RegistroRequest.class);
                Erro erro = new Erro();
                PassFine res = new PassFine();

                cliente.setCpf(UserData.getCpf());
                cliente.setSenha(UserData.getPassword());
                cliente.setNome(UserData.getNome());

                if (((!cliente.getCpf().isEmpty() && !cliente.getSenha().isEmpty())) && !cliente.getNome().isEmpty()) {
                    String validData = verifica.VerificaDados(true);
                    switch (validData) {
                        case "cpf":
                            erro.setResponse(
                                    "Cpf Invalido Verifique, cpf Deve seguir esse padrão (000.000.000-00) e ou Cpf esta incorreto");
                            // verificar se cpf é valido
                            break;
                        case "cpfExiste":
                            erro.setResponse("Cpf ja Cadastrado no Sistema faça login"); // verificar se cpf existe no
                                                                                         // banco
                            break;
                        case "senha":
                            erro.setResponse("Senha Deve Ter mais de 8 digitos e nao deve ter mais de 16");
                        default:
                            break;
                    }
                } else {
                    erro.setResponse("Nenhum Campo pode estar vazio");
                }

                if (erro.getResponse().isEmpty()) {
                    CadastroUsuario.CadastrarUsuario();
                    res.setMessage("Usuario Cadastrado Com Sucesso");
                    String rs = gson.toJson(res);
                    exchange.getResponseHeaders().set("Contente-type", "application/json");
                    exchange.sendResponseHeaders(201, rs.getBytes().length); // codigo 201 para informar que foi feito a mudança
                    OutputStream os = exchange.getResponseBody();
                    os.write(rs.getBytes());
                    os.close();
                } else {
                    String resErro = gson.toJson(erro);
                    exchange.getResponseHeaders().set("Content-type", "application/json");
                    exchange.sendResponseHeaders(200, resErro.getBytes().length); // 200 informando que deu certo na conexão mas algo deu errado na hora de inserir os dados
                    OutputStream os = exchange.getResponseBody();
                    os.write(resErro.getBytes());
                    os.close();
                }

            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    public class Despositar implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            Cliente cliente = new Cliente();

            if ("POST".equals(exchange.getRequestMethod())) {

                Gson gson = new Gson();
                InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                DespotioRequest UserData = gson.fromJson(reader, DespotioRequest.class);
                
                Erro erro = new Erro();
                PassFine res = new PassFine();

                String cpf = UserData.getCpf();
                double valor = UserData.getValor();
                boolean logado = false;

                if (!(valor == 0.0) && !cpf.isEmpty()) {
                    logado = EstaLogado.Logado();
                    if (!logado) {
                        erro.setResponse("é necessario estar logado em uma conta para realizar essa operação");
                    }

                    while (logado) {
                        if (!jwt.ValidaToken(cliente.getTokenSession())) {
                            erro.setResponse("Token De Sessão invalido Faça login novamente");
                            break;
                        }
                        boolean DespitoFeito = Deposito.DepositoConta(cpf, valor);
                        if (!DespitoFeito) {
                            erro.setResponse("Algo Deu Errado na operação de deposito Verifique os dados");
                            break;
                        }
                        break;
                    }
                } else {
                    erro.setResponse("cpf nao pode ser vazio e/ou Valor de deposito deve ser maior que 0");
                }

                if (erro.getResponse().isEmpty()) {
                    res.setMessage("Deposito realizado com sucesso");
                    String rs = gson.toJson(res);
                    exchange.getResponseHeaders().set("Contente-type", "application/json");
                    exchange.sendResponseHeaders(201, rs.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(rs.getBytes());
                    os.close();
                } else {
                    String resErro = gson.toJson(erro);
                    exchange.getResponseHeaders().set("Contente-type", "application/json");
                    exchange.sendResponseHeaders(301, resErro.getBytes().length); // Nao permitido
                    OutputStream os = exchange.getResponseBody();
                    os.write(resErro.getBytes());
                    os.close();
                }
            }
        }
    }

    public class PegaSaldo implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            Cliente cliente = new Cliente();
            if ("GET".equals(exchange.getRequestMethod())) {
                
                Gson gson = new Gson();
                Erro erro = new Erro();
                PassFine res = new PassFine();

                boolean GetSaldoOk = true;
                boolean logado = EstaLogado.Logado();
                if (!logado) {
                    erro.setResponse("Voce deve estar logado para realizar consultas");
                }

                if (logado) {
                    boolean TokenValido = false;
                    do {
                        if (!(jwt.ValidaToken(cliente.getTokenSession()))) {
                            erro.setResponse("token Exprirado faça login novamente");
                            break;
                        }
                        GetSaldoOk = Saldo.ConsultaSaldo();
                        break;

                    } while (TokenValido);
                }
                if (!GetSaldoOk) {
                    erro.setResponse("Tente novamente algo deu erra na manipulação dos dados");
                }
                if (erro.getResponse().isEmpty()) {
                    res.setMessage(Double.toString(cliente.getConta().getSaldo()));
                    String rs = gson.toJson(res);
                    exchange.getResponseHeaders().set("Content-type", "application/json");
                    exchange.sendResponseHeaders(200, rs.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(rs.getBytes());
                    os.close();
                } else {
                    String resErro = gson.toJson(erro);
                    exchange.sendResponseHeaders(200, resErro.getBytes().length);
                    exchange.getResponseHeaders().set("Content-type", "application/json");
                    OutputStream os = exchange.getResponseBody();
                    os.write(resErro.getBytes());
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

                Erro erro = new Erro();
                PassFine res = new PassFine();

                double valor = UserData.getValor();

                double SaqueMon = 0.0;
                boolean logado = EstaLogado.Logado();

                if (!logado) {
                    erro.setResponse("Voce deve estar logado para realizar consultas");
                }

                if (logado) {
                    boolean TokenValido = false;
                    do {
                        if (!(jwt.ValidaToken(cliente.getTokenSession()))) {
                            erro.setResponse("token Exprirado faça login novamente");
                            break;
                        }
                        if (valor <= 0) {
                            erro.setResponse("Impossivel sacar esse valor.");
                            break;
                        }
                        SaqueMon = Saque.Sacar(valor);
                        break;

                    } while (TokenValido);
                }
                if (SaqueMon == -1.0) {
                    erro.setResponse("Saldo insuficiente Para Relizar um saque");
                }  

                if (erro.getResponse().isEmpty()) {
                    //Retorna o valor do saque;
                    res.setMessage(Double.toString(SaqueMon));
                    String rs = gson.toJson(res);
                    exchange.getResponseHeaders().set("Content-type", "application/json");
                    exchange.sendResponseHeaders(200, rs.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(rs.getBytes());
                    os.close();
                } else {
                    String Errores = gson.toJson(erro);
                    exchange.getResponseHeaders().set("Content-type", "application/json");
                    exchange.sendResponseHeaders(200, Errores.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(Errores.getBytes());
                    os.close();
                }
            }
        }
    }

    public class SairConta implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                boolean logado = EstaLogado.Logado();
                
                Gson gson = new Gson();
                Erro erro = new Erro();
                PassFine res = new PassFine();

                if (!logado) {
                    erro.setResponse("Voce deve estar logado para realizar consultas");
                }

                if (logado) {
                    SairDaConta.sair();
                }

                if (erro.getResponse().isEmpty()) {
                    res.setMessage("Saindo");
                    String rs = gson.toJson(res);
                    exchange.getResponseHeaders().set("Content-type", "application/json");
                    exchange.sendResponseHeaders(200, rs.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(rs.getBytes());
                    os.close();
                } else {
                    String ResErro = gson.toJson(erro);
                    exchange.getResponseHeaders().set("Content-type", "application/json");
                    exchange.sendResponseHeaders(200, ResErro.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(ResErro.getBytes());
                    os.close();
                }
            }
        }
    }

    public class DeletarConta implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            Cliente cliente = new Cliente();
            if ("GET".equals(exchange.getRequestMethod())) {
                boolean logado = EstaLogado.Logado();

                Gson gson = new Gson();
                Erro erro = new Erro();
                PassFine res = new PassFine();

                if (!logado) {
                    erro.setResponse("Voce deve estar logado para realizar consultas");
                }

                while (logado) {
                    if (jwt.ValidaToken(cliente.getTokenSession())) {
                        boolean TemSaldo = com.nicolas.Operacoes.DeletarConta.Deletar();
                        System.err.println(TemSaldo);
                        if (!TemSaldo) {
                            erro.setResponse("Nao é possivel deletar uma conta que possui saldo nela");
                        }
                        break;
                    } else {
                        erro.setResponse("Token invalido faca login novamete");
                    }
                    break;
                }

                if (erro.getResponse().isEmpty()) {

                    res.setMessage("Conta Deletada Com sucesso");
                    String rs = gson.toJson(res);

                    exchange.sendResponseHeaders(200, rs.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(rs.getBytes());
                    os.close();
                } else {
                    String ErroRes = gson.toJson(erro);
                    exchange.sendResponseHeaders(200, ErroRes.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(ErroRes.getBytes());
                    os.close();
                }
            }
        }
    }
}
