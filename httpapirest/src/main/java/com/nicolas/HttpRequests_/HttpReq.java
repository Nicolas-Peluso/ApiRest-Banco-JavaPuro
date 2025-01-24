package com.nicolas.HttpRequests_;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.nicolas.Manipulacao.Seleciona;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class HttpReq {

    public void StartServer() throws IOException{
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        server.createContext("/nome", new HelloHandler());
 
        System.out.println("Servidor iniciado na porta 8080");
        server.start();
    }

    class HelloHandler implements HttpHandler {
        
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                String response = Seleciona.SelectNome();

                if(!response.isEmpty()){
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody(); 
                    os.write(response.getBytes());
                    os.close();
                }

            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }
    
}   
