package libs;

import java.io.*;
import java.net.*;

public class ClientHandler extends Thread {

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream()));
        start();
    }

    @Override
    public void run() {
        handle();
    }

    public void handle(){
        String data;
        try {
            while (true) {
                data = read();
                if (data == null) {
                    downService();
                    break;
                }
                System.out.println(data);
            }
        } catch (IOException ignored) {
            downService();
        }
    }

    private String read() throws IOException {
        return in.readLine();
    }

    public String getIp(){
        return socket.getRemoteSocketAddress()
                .toString()
                .trim()
                .replace("/", "");
    }

    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {}
    }

    private void downService() {
        try {
            if(!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                for (ClientHandler vr : Clients.list) {
                    if(vr.equals(this)) vr.interrupt();
                    Clients.list.remove(this);
                }
            }
            System.out.println("Client disconnected");
        } catch (IOException ignored) {}
    }

}