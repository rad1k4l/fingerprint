package libs;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import handler.DeviceMessageHandler;
import handler.DeviceRequestHandler;
import handler.IHandleable;
import org.bson.Document;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class Server extends WebSocketServer {

    public Server(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("new connection from " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("closed ");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("received "	+ conn.getRemoteSocketAddress() + " : " + message);
        Document data = Document.parse(message);
        IHandleable handler = null;
        if (data.containsKey("cmd")){
            handler = new DeviceMessageHandler();
        }else if(data.containsKey("ret")){
            handler = DeviceRequestHandler.getInstance();
        }
        if (handler != null)
            try {
                String response = handler.setData(data).handle(conn);
                if (response != null){
                    System.out.println("Send: " + response);
                    conn.send(response);
                }
            } catch (Exception e) {
                System.out.println("Error exception " + e.getMessage());
            }
        else {
            System.out.println("Switching to ON_STATE_ERROR");
        }
    }

    @Override
    public void onMessage( WebSocket conn, ByteBuffer message ) {
        System.out.println("received ByteBuffer from "	+ conn.getRemoteSocketAddress());
        System.out.println("ByteBuffer ignored");
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("an error occurred on connection " + conn.getRemoteSocketAddress()  + ":" + ex);
    }

    @Override
    public void onStart() {
        System.out.println("server started successfully");
    }
}