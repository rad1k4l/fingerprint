package request;

import controllers.IControllable;
import handler.DeviceRequestHandler;
import model.DeviceUser;
import org.bson.Document;
import org.java_websocket.WebSocket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class UserRequest extends BaseRequest {


    public void getUserList(WebSocket connection, IControllable callable) {

        String cmd = "getuserlist";
        connection.send(cmd(cmd)
                .append("stn", true)
                .toJson());
        DeviceRequestHandler.getInstance().on(cmd, callable);
    }

    public void getUserInfo(WebSocket connection, IControllable callable, int enrollid, int backupnum) {
        connection.send(cmd("getuserinfo")
                .append("enrollid", enrollid)
                .append("backupnum", backupnum)
                .toJson());
        DeviceRequestHandler.getInstance().on("getuserinfo", callable);
    }

    public void setUserInfo(WebSocket connection, IControllable callable, DeviceUser user) {
        String cmd = "setuserinfo";

        String payload = cmd(cmd)
                .append("enrollid", user.enrollid)
                .append("name", user.name)
                .append("backupnum", user.backupnum)
                .append("admin", user.admin)
                .append("record", user.record)
                .toJson();
        System.out.println(payload);
        connection.send(payload);
        DeviceRequestHandler.getInstance().on(cmd, callable);
    }

    public void deleteUser(WebSocket connection, IControllable callable, DeviceUser user) {
        String cmd = "deleteuser";
        connection.send(cmd(cmd)
                .append("enrollid", user.enrollid)
                .append("backupnum", user.backupnum)
                .toJson());
        DeviceRequestHandler.getInstance().on(cmd, callable);
    }

    public void getUsername(WebSocket connection, IControllable callable, DeviceUser user) {
        String cmd = "getusername";
        String payload = cmd(cmd)
                .append("enrollid", user.enrollid)
                .toJson();
        System.out.println(payload);
        connection.send(payload);
        DeviceRequestHandler.getInstance().on(cmd, callable);
    }

    public void setUsername(WebSocket connection, IControllable callable, DeviceUser user) {
        String cmd = "setusername";
        List<Document> users = new ArrayList<>();
        users.add(new Document("enrollid", user.enrollid));
        users.add(new Document("name", user.name));
        String payload = cmd(cmd)
                .append("count", 1)
                .append("record", users)
                .toJson();
        System.out.println(payload);
        connection.send(payload);
        DeviceRequestHandler.getInstance().on(cmd, callable);
    }

    public void cleanAll(WebSocket connection, IControllable callable) {
        String cmd = "cleanuser";
        connection.send(cmd(cmd)
                .toJson());
        DeviceRequestHandler.getInstance().on(cmd, callable);
    }

}
