package handler;

import controllers.IControllable;
import exceptions.CmdNotFoundException;
import org.bson.Document;
import org.java_websocket.WebSocket;

import java.util.HashMap;
import java.util.Map;

public class DeviceRequestHandler implements IHandleable {

    private static volatile Map<String, IControllable> actions;
    static {
        actions = new HashMap<>();
    }
    private static DeviceRequestHandler instance;
    private static Document data;

    private DeviceRequestHandler (){ }

    public static DeviceRequestHandler getInstance() {
        if (instance !=null)
            return instance;
        else
            return setInstance(new DeviceRequestHandler());
    }

    private static DeviceRequestHandler setInstance(DeviceRequestHandler instance) {
        DeviceRequestHandler.instance = instance;
        return instance;
    }

    public DeviceRequestHandler setData(Document json){
        data = json;
        return this;
    }

    public DeviceRequestHandler on(String ret, IControllable obj){
        actions.put(ret, obj);
        return this;
    }

    public IHandleable removeAction(String cmd) {
        actions.remove(cmd);
        return this;
    }

    public String handle(WebSocket connection) throws Exception{
        String cmd = data.get("ret", String.class);
        if (cmd != null)
            return actions
                    .get(cmd)
                    .handle(data, connection);
        else
            throw new CmdNotFoundException("Cmd not found");
    }


}