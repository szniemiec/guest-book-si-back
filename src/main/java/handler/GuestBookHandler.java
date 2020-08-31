package handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.MessageDaoImpl;
import helpers.DataFormParser;
import models.Message;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GuestBookHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String response = "";
        int rCode = 0;
        MessageDaoImpl messageDao = new MessageDaoImpl();
        if (requestMethod.equals("GET")) {
            try {
                List<Message> messageList = messageDao.getAll();
                ObjectMapper mapper = new ObjectMapper();
                response = mapper.writeValueAsString(messageList);
                rCode = 200;
            } catch (Exception e) {
                response = e.getMessage();
                rCode = 401;
            }
        } else if (requestMethod.equals("POST")) {
            DataFormParser dataFormParser = new DataFormParser();
            Map<String, String> data = dataFormParser.getData(exchange);
            String name = data.get("name");
            String message = data.get("message");
            System.out.println(name + " " + message);
            Message messageObject = new Message()
                    .setName(name)
                    .setMessage(message);
            try {
                messageDao.add(messageObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (rCode == 200) {
            exchange.getResponseHeaders().put("Content-type", Collections.singletonList("application/json"));
            exchange.getResponseHeaders().put("Access-Control-Allow-Origin", Collections.singletonList("*"));
        }
        exchange.sendResponseHeaders(rCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}