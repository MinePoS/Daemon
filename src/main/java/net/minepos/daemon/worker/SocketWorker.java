package net.minepos.daemon.worker;

import net.minepos.daemon.Main;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshakeBuilder;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class SocketWorker extends WebSocketServer {
public HashMap<String, WebSocket> keyMap;
    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String apikey;

    public SocketWorker(InetSocketAddress address) {
        super(address);
        keyMap = new HashMap<String, WebSocket>();
    }

    @Override
    public ServerHandshakeBuilder onWebsocketHandshakeReceivedAsServer(WebSocket conn, Draft draft, ClientHandshake request) throws InvalidDataException {
        ServerHandshakeBuilder builder = super.onWebsocketHandshakeReceivedAsServer( conn, draft, request );
        //To your checks and throw an InvalidDataException to indicate that you reject this handshake.
        if(!request.getResourceDescriptor().contains(":")){
            System.out.println("Denying access due to invalid data");

            throw new InvalidDataException( CloseFrame.POLICY_VALIDATION, "Not accepted!");
        }
        String path = request.getResourceDescriptor();
        if(path.startsWith("/")){
            path = path.replaceFirst("/","");
        }

        String name = path.split(":")[0];
        String apikey = path.split(":")[1];
        String url = Main.minepos+"/checkapikey?name="+name+"&key="+apikey;

        try {
            String res = getHTML(url);
            if(res.equalsIgnoreCase("deny")){
                System.out.println("Denying access due to Store not accepting the API key");
                throw new InvalidDataException( CloseFrame.POLICY_VALIDATION, "Not accepted!");
            }else{
                System.out.println("Connection has been Authenticated by store, API KEY is valid for use by: "+res);
                conn.setAttachment(res);
                if(keyMap.containsKey(apikey)){
                    keyMap.get(apikey).close(CloseFrame.UNEXPECTED_CONDITION, "Logged in from another location");
                    keyMap.remove(apikey);
                }
                keyMap.put(apikey, conn);
            }
        } catch (Exception e) {
            if(!(e instanceof InvalidDataException)) {
                System.out.println("Denying access due to not being able to connect to Store");
                throw new InvalidDataException(CloseFrame.POLICY_VALIDATION, "Not accepted!");
            }
        }


        return builder;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        //conn.send("Welcome to the server!"); //This method sends a message to the new client
        //broadcast( "new connection: " + handshake.getResourceDescriptor() ); //This method sends a message to all clients connected
        System.out.println("new connection to " + conn.getRemoteSocketAddress());
        System.out.println("Descriptor Is" + handshake.getResourceDescriptor());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("received message from "	+ conn.getAttachment());
        //broadcast( "received message from "	+ conn.getAttachment() + ": " + message);
        if(!message.equalsIgnoreCase("heartbeat")){
            JSONObject jsonObject = new JSONObject(message);
            if(jsonObject.has("send_to")) {
                String sendTo = jsonObject.getString("send_to");
                if (keyMap.containsKey(sendTo)) {
                    keyMap.get(sendTo).send(message);
                    System.out.println("Forwarded to: " + keyMap.get(sendTo).getAttachment());
                }
            }else{
                System.out.println("The message is for the daemon: "+ message);
                if(jsonObject.getString("action").equalsIgnoreCase("get_command_queue")){
                    String thisAPIKey = "";
                    for (Map.Entry<String, WebSocket> entry : keyMap.entrySet()) {
                        if (entry.getValue() == conn) {
                            thisAPIKey = entry.getKey();
                        }
                    }

                    String url = Main.minepos+"/api/getQueue?key="+thisAPIKey;
                    System.out.println(url);
                    try {
                        getHTML(url);
                    } catch (Exception e) {

                    }
                }
                if(jsonObject.getString("action").equalsIgnoreCase("command-reply")){
                    String thisAPIKey = "";
                    for (Map.Entry<String, WebSocket> entry : keyMap.entrySet()) {
                        if (entry.getValue() == conn) {
                            thisAPIKey = entry.getKey();
                        }
                    }

                    String url = Main.minepos+"/api/commandDone/"+jsonObject.getInt("queued_id")+"?key="+thisAPIKey;
                    if(jsonObject.getBoolean("response")) {
                        System.out.println("Command "+jsonObject.getInt("queued_id")+ " is done sending to site.");
                        try {
                            getHTML(url);
                        } catch (Exception e) {
                            System.out.println("Couldn't mark "+jsonObject.getInt("queued_id")+ " as done!");
                        }
                    }
                }
            }

        }
    }

    @Override
    public void onMessage( WebSocket conn, ByteBuffer message ) {
        String converted = null;
        try {
            converted = new String(message.array(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.onMessage(conn, converted);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("an error occured on connection " + conn.getRemoteSocketAddress()  + ":" + ex);
    }

    @Override
    public void onStart() {
        System.out.println("Server started successfully");
        System.out.println("  - Daemon Store: "+Main.minepos);
    }


    public String getHTML(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }
}
