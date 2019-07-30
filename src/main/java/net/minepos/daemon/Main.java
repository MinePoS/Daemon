package net.minepos.daemon;

import net.minepos.daemon.worker.SocketWorker;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class Main {

    public static String minepos = "https://demo.minepos.net";

    public static void main(String[] args) {
        String host = "0.0.0.0";
        int port = 8887;

        WebSocketServer s = new SocketWorker(new InetSocketAddress(host, port));
        s.setConnectionLostTimeout(0);
        s.run();
    }
}
