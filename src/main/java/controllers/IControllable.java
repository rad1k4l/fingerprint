package controllers;

import org.bson.Document;
import org.java_websocket.WebSocket;

public interface IControllable {
    String response = null;
    String handle(Document data, WebSocket connection);
}