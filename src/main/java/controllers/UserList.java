package controllers;

import org.bson.Document;
import org.java_websocket.WebSocket;

public class UserList extends Controller implements IControllable {

    @Override
    public String handle(Document request, WebSocket connection){
        return ret("getuserlist").toJson();
    }
}
