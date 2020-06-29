package controllers;

import org.bson.Document;
import org.java_websocket.WebSocket;

public class LogController extends Controller implements IControllable {
    public Document response = null;

    @Override
    public String handle(Document request, WebSocket connection) {
        System.out.println("[X] Received log");
        return ret("sendlog").toJson();
    }




}
