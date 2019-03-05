package game;

import java.beans.ExceptionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class PeerSender extends Thread {

    public Socket connection;

    private PeerReceiver receiver;
    private String ip;

    public PeerSender(String ip) {
        this.ip = ip;
    }

    private void connect() {
        try {
            System.out.println("Connect method: " + connection);
            if (!isConnected()) {
                System.out.println("Calling " + ip + "...");
                connection = new Socket();
                connection.connect(new InetSocketAddress(ip, 6666), 2000);
                System.out.println("Call connected with " + ip + "!");
            } else {
                return;
            }
        } catch (SocketTimeoutException e) {}
        catch (IOException e) {
            System.out.println(String.format("PeerSender[%s]::%s::%s", ip, e.getClass(), e.getMessage()));
        }

        connect();
    }

    private void send() {
        try {
            DataOutputStream output = new DataOutputStream(connection.getOutputStream());
            while(true) {
                output.writeBytes(Main.name + "\n");
            }
        } catch (IOException e) {
            System.out.println(String.format("PeerSender[%s]::%s::%s", ip, e.getClass(), e.getMessage()));
        }
    }

    private void donate() {
        if (receiver != null) {
            try {
                receiver.handshaker.close();
                receiver.giveConnection(connection);
            } catch (IOException e) {
                System.out.println(String.format("PeerSender[%s]::%s::%s", ip, e.getClass(), e.getMessage()));
            }
        } else {
            donate();
        }
    }

    @Override
    public void run() {
        connect();
        donate();
        send();
    }

    public boolean isConnected() {
        return (connection != null) && connection.isConnected();
    }

    public void giveConnection(Socket connection) {
        this.connection = connection;
    }

    public void setReceiver(PeerReceiver receiver) {
        this.receiver = receiver;
    }

}
