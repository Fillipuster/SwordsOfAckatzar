package game;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class PeerConnection extends Thread {

    private String ip;
    private Socket conn;

    public PeerConnection(String ip) throws Exception {
        this.ip = ip;
    }

    @Override
    public void run() {
        while (!isConnected()) {
            try {
                conn = new Socket(ip, 6666);
            } catch (IOException e) {
                System.out.println("Klamydia.");
            }
        }

        while(true) {
            try {
                System.out.println("Active conection with: " + ip);
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("Whatever");
            }
        }
    }

    public boolean isConnected() {
        return conn.isConnected();
    }

    public void giveConnection(Socket conn) {
        this.conn = conn;
    }

    public String getIP() {
        return ip;
    }
}
