package request;

import controllers.IControllable;
import handler.DeviceRequestHandler;
import org.java_websocket.WebSocket;

public class DeviceSystemRequest extends BaseRequest  {

    public void reboot(WebSocket connection, IControllable callable) {
        String cmd = "reboot";
        connection.send(cmd(cmd)
                .toJson());
        DeviceRequestHandler.getInstance().on(cmd, callable);
    }

    public void setTime(WebSocket connection, IControllable callable, String time) {
        String cmd = "settime";
        connection.send(cmd(cmd)
                .append("cloudtime", time)
                .toJson());
        DeviceRequestHandler.getInstance().on(cmd, callable);
    }
}
