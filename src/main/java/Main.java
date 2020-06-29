import controllers.LogController;
import controllers.RegController;
import controllers.SendUserController;
import controllers.UserList;
import handler.DeviceMessageHandler;
import handler.DeviceRequestHandler;
import handler.SystemTerminateHandler;
import libs.*;
import model.DeviceUser;
import org.java_websocket.WebSocket;
import org.java_websocket.server.WebSocketServer;
import request.BaseRequest;
import request.UserRequest;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Scanner;


import java.time.Duration;
import java.time.Instant;


import java.io.*;
import java.util.StringTokenizer;
import java.util.stream.Stream;

import static java.lang.System.*;

public class Main {

    private static String[] arg;
    static String message;
    public static void main(String[] args) throws Exception {
         print();
//        sysStart(args); // system start
    }
    static void print(){
        try{
            System.out.print("Hello");
        }finally{
            System.out.println("!");
        }
    }

    public static void sysStart(String[] args) throws Exception {
        arg = args;
        registerRoutes();
        Thread terminal = new Thread(() -> {
            System.out.println("thread started");
            Scanner scanner = new Scanner(System.in);
            System.out.println("terminal started...");
            DeviceUser savedUser = new DeviceUser();
            while (true){
                String cmd = scanner.nextLine();
                if (cmd.equals("exit")){
                    System.out.println("preparing exit");
//                    System.out.println("cmd: thr.stop()");
//                    Thread.currentThread().stop();
                    System.out.println("closing connections ");
                    if (SharedMemory.deviceSocket != null)
                        SharedMemory.deviceSocket.close();

                    for (WebSocket user:
                            SharedMemory.frontendUsersSocket){
                        user.close();
                    }

                    System.out.println("cmd: exit(0)");
                    System.exit(0);
                }
                else if (cmd.equals("getusers")){
                    System.out.println("user request");
                    int iterator = 0;
                    if (SharedMemory.deviceSocket != null && SharedMemory.deviceSocket.isOpen())
                        new UserRequest().getUserList(SharedMemory.deviceSocket, (data, conn) -> {
                            if (data.getInteger("count", 0) == data.getInteger("to",0)){
                                System.out.println("USERINFO end");
                                DeviceRequestHandler.getInstance().removeAction("getuserlist");
                                return null;
                            }
                            return BaseRequest.cmd("getuserlist").append("stn", false).toJson();
                        });
                    else System.out.println("Error mode: DEVICE_NOT_CONNECTED");
                }
                else if (cmd.equals("getuserinfo")){
                    System.out.println("enrollid: ");
                    int enrollid = scanner.nextInt();
                    System.out.println("backupnum: ");
                    int backupnum = scanner.nextInt();
                    System.out.println("user request");
                    int iterator = 0;
                    if (SharedMemory.deviceSocket != null && SharedMemory.deviceSocket.isOpen())
                        new UserRequest().getUserInfo(SharedMemory.deviceSocket, (data, conn) -> {
                            System.out.println("received userinfo");
                            savedUser.enrollid = data.getInteger("enrollid");
                            savedUser.name = data.getString("name");
                            savedUser.backupnum = data.getInteger("backupnum");
                            if (data.containsKey("record")) {
                                savedUser.record = data.get("record", String.class);
                            }else System.out.println("user record not found");
                            savedUser.admin = data.getInteger("admin");
                            System.out.println("user saved");
                            return null;
                        },enrollid,backupnum);
                    else System.out.println("Error: DEVICE_NOT_CONNECTED");
                }
                else if (cmd.equals("setuserinfo")){
                    scanner.next();
                    DeviceUser user = new DeviceUser();
                    System.out.println("enrollid");
                    user.enrollid = scanner.nextInt();
                    System.out.println("name");
                    user.name = "orxan";
                    System.out.println("backupnum auto set");
                    user.backupnum = 0;
                    System.out.println("admin auto set");
                    user.admin = 0;
                    System.out.println("record auto set");
                    user.record = savedUser.record;
                    System.out.println("send request");
                    new UserRequest()
                            .setUserInfo(SharedMemory.deviceSocket,(data, connection)->{ return null; }, user);
                }
                else if (cmd.equals("cleanall")){
                    new UserRequest()
                            .cleanAll(SharedMemory.deviceSocket,(data, connection) -> {return null;});
                    System.out.println("delete request sent");
                }
                else if (cmd.equals("getusername")){
                    DeviceUser user = new DeviceUser();
                    System.out.println("enrollid: ");
                    user.enrollid = scanner.nextInt();
                    new UserRequest()
                            .getUsername(SharedMemory.deviceSocket,(data,connection)->{ return null; }, user);
                }
                else if (cmd.equals("deleteuser")){
                    DeviceUser user = new DeviceUser();

                    System.out.println("enrollid");
                    user.enrollid = scanner.nextInt();
                    System.out.println("backupnum");
                    user.backupnum = scanner.nextInt();

                    new UserRequest().deleteUser(SharedMemory.deviceSocket,(data,conn) -> {
                        return null;
                    }, user);
                }
                else if (cmd.equals("setusername")){
                    DeviceUser user = new DeviceUser();
                    System.out.println("username:");
                    user.name = scanner.nextLine();
                    System.out.println("enrollid");
                    user.enrollid = scanner.nextInt();
                    new UserRequest()
                            .setUsername(SharedMemory.deviceSocket, (data,conn) -> {
                                return null;
                            }, user);
                }
                else if (cmd.equals("connection")) {
                    System.out.println(
                            SharedMemory.deviceSocket != null && SharedMemory.deviceSocket.isOpen()
                                    ? "CONNECTED" : "NOT_CONNECTED" );
                }
                else if (cmd.equals("backup:users")){
                    new UserRequest().getUserList(SharedMemory.deviceSocket,(data,connection )-> {

                        return null;
                    });
                }



            }
        });
        SharedMemory.threads.push(terminal);
        terminal.start();

        websocket();
        System.out.println("[X] websocket started.");
    }

    private static void registerRoutes(){
        System.out.println("[X] register incoming routes...  [DONE]");
        new DeviceMessageHandler()
                .on("reg", new RegController())
                .on("sendlog", new LogController())
                .on("senduser", new SendUserController());
    }

    private static void websocket() throws Exception{
        String host = "0.0.0.0";
        int port = 7788;
        WebSocketServer server = new Server(new InetSocketAddress(host, port));
        server.run();
    }

    private static void tcp() throws Exception{
        int port = 7788;
        String ip = "0.0.0.0";
        ServerSocket serverSocket = new ServerSocket(port, 1, InetAddress.getByName(ip));
        System.out.println("Listening on " + port + " addr " + ip);
        while (true) {
            Socket client = serverSocket.accept();
            System.out.println("client connected ! ");
            Clients.list.add(new ClientHandler(client));
        }
    }
}
