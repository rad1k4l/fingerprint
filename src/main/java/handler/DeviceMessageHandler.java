package handler;

import controllers.IControllable;
import exceptions.CmdNotFoundException;
import org.bson.Document;
import org.java_websocket.WebSocket;

import java.util.HashMap;
import java.util.Map;

public class DeviceMessageHandler implements IHandleable {
    private static volatile Map<String, IControllable> actions;
    static {
        actions = new HashMap<>();
    }

    public IHandleable removeAction(String cmd) {
        actions.remove(cmd);
        return this;
    }

    private static Document data;

    public DeviceMessageHandler setData(Document json){
        data = json;
        return this;
    }

    public DeviceMessageHandler on(String cmd, IControllable obj){
        actions.put(cmd, obj);
        return this;
    }

    public String handle(WebSocket connection) throws Exception{
        String cmd = data.get("cmd", String.class);
        if (cmd != null)
            return actions
                    .get(cmd)
                    .handle(data, connection);
        else
            throw new CmdNotFoundException("Cmd not found");
    }

}
