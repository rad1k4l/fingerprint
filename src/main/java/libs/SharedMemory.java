package libs;

import org.java_websocket.WebSocket;

import java.util.LinkedList;

public class SharedMemory {
    public static LinkedList<WebSocket> frontendUsersSocket = new LinkedList<>();
    public volatile static WebSocket deviceSocket;

    public static LinkedList<Thread> threads = new LinkedList<>();

}