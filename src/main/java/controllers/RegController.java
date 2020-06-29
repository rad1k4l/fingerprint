package controllers;

import libs.SharedMemory;
import org.bson.Document;
import org.java_websocket.WebSocket;

public class RegController extends Controller implements IControllable {

    @Override
    public String handle(Document data, WebSocket connection) {
        System.out.println("[X] Device registered");
        SharedMemory.deviceSocket = connection;
        return ret("reg").toJson();
    }
}
