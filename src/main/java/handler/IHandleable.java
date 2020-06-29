package handler;

import org.bson.Document;
import org.java_websocket.WebSocket;

public interface IHandleable {


    IHandleable setData(Document json);
    String handle(WebSocket connection) throws Exception;
    IHandleable removeAction(String cmd);
}
