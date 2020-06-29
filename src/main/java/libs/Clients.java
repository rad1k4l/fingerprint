package libs;

import java.util.LinkedList;

public class Clients {

    public static LinkedList<ClientHandler> list = new LinkedList<>();

    public static int count()
    {
        return list.size();
    }
}