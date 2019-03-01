package game;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class PeerReceiver extends Thread {

    public Socket connection;
    public ServerSocket handshaker;

    private PeerSender sender;
    private String ip;

    public PeerReceiver(String ip) {
        this.ip = ip;
    }

    private void connect() {
        try {
            System.out.println("Listening for " + ip + "...");
            handshaker.accept();
        } catch (IOException e) {
            System.out.println(String.format("PeerReceiver[%s]::%s::%s", ip, e.getClass(), e.getMessage()));
        }
    }

    private void receive() {
        // Receive
    }

    private void donate() {
        if (sender != null) {
            sender.giveConnection(connection);
        } else {
            donate();
        }
    }

    @Override
    public void run() {
        try {
            handshaker = new ServerSocket(6666);
        } catch (IOException e) {
            System.out.println(String.format("PeerReceiver[%s]::%s::%s", ip, e.getClass(), e.getMessage()));
        }

        connect();
        donate();
        receive();
    }

    public boolean isConnected() {
        return (connection != null) && connection.isConnected();
    }

    public void giveConnection(Socket connection) {
        this.connection = connection;
    }

    public void setSender(PeerSender sender) {
        this.sender = sender;
    }

}
