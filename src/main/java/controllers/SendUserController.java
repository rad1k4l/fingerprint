package controllers;

import org.bson.Document;
import org.java_websocket.WebSocket;

public class SendUserController extends Controller implements IControllable {
    public String response;
    @Override
    public String handle(Document request, WebSocket connection) {
        return ret("senduser").toJson();
    }
}
