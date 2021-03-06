package it.polimi.ingsw.network.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.polimi.ingsw.model.InterfaceAdapter;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.network.Utilities;
import it.polimi.ingsw.network.messages.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    private final int id;

    private final Socket socket;
    private Server server;
    private Thread pinger;
    private Thread timer;

    private Scanner in;
    private PrintWriter out;

    private Gson gson;

    private boolean isConnected;
    private boolean ping;
    private boolean timeout;
    private final ServerVisitorHandler serverVisitorHandler;
    private Controller lobby;

    private static int globalCounter = 0;

    public Controller getLobby() { return lobby; }

    public void setLobby(Controller lobby) { this.lobby = lobby; }

    public int getId() {
        return id;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Thread getPinger() {
        return pinger;
    }

    public Server getServer() {
        return server;
    }

    public Gson getGson() {
        return gson;
    }

    public Scanner getIn() { return in; }

    public PrintWriter getOut() { return out; }

    public boolean isConnected() {
        return isConnected;
    }

    public boolean isTimeout() {
        return timeout;
    }

    public void setPing(boolean ping) {
        this.ping = ping;
    }

    public void setConnected(boolean connected) {
        this.isConnected = connected;
    }

    public void send(Message message) { //it's one line but it's nice to have for other classes
        out.println(gson.toJson(message, Message.class));
    }

    public void stopTimer(){
        if(timer != null){
            timer.interrupt();
            timer = null;
        }
    }
    public void startPinger(){
        if(isConnected) {
            this.pinger = new Thread(() -> {
                ping = true;
                while (ping) {
                    try {
                        ping = false;
                        send(new PingRequest());
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
                System.out.println(id+" Player disconnected");
                isConnected = false;
                server.handleDisconnection(id);
            });
            this.pinger.start();
        }
    }
    public void stopPinger(){
        if(isConnected) {
            if (pinger != null) {
                pinger.interrupt();
                pinger = null;
            }
        }
    }
    public void startTimer(int ms){
        timer = new Thread(() -> {
            try {
                Thread.sleep(ms);
                timeout = true;
                endConnection();
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
        });
        timer.start();
    }

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        this.isConnected = true;
        this.serverVisitorHandler = new ServerVisitorHandler();
        globalCounter++;
        this.id = globalCounter;
        this.timeout = false;
    }

    public void run() {
        gson = Utilities.initializeGsonMessage();

        try {
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);

            send(new RegisterRequest());
            startTimer(50000);

            String jsonString = "";
            while (!jsonString.equals("quit")) {
                try {
                    jsonString = in.nextLine();
                } catch (NoSuchElementException | IllegalStateException e){
                    System.out.println("Disconnecting client handler number " + id + " . . .");
                    return;
                }
                ServerMessage message=gson.fromJson(jsonString, ServerMessage.class);
                message.accept(serverVisitorHandler,this);
            }
            // closing streams and socket
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public ServerVisitorHandler getServerVisitorHandler() {
        return serverVisitorHandler;
    }
    public void endConnection() throws InterruptedException {
        stopPinger();
        send(new DisconnectionMessage());
        out.close();
        in.close();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.removeFromWaitingList(id);
        System.out.println("Disconnected user with id: " + id);
    }
    public void closeConnection() throws InterruptedException {
        send(new DisconnectionMessage());
        server.removeVirtualClient(id);
        out.close();
        in.close();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Disconnected user with id: " + id);
    }
}