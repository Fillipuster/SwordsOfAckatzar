package game;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
        try {
            while (!isConnected()) {
                conn = new Socket(ip, 6666);
            }

            BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            DataOutputStream output = new DataOutputStream(conn.getOutputStream());
            while(true) {
                output.writeBytes(Main.name + "\n");
                String str  = input.readLine();
                if (str != null) {
                    System.out.println(conn.getInetAddress().toString() + " is called: " + str);
                }
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            System.out.println("HIV");
        }
    }

    public boolean isConnected() {
        if (conn == null) {
            return false;
        } else {
            return conn.isConnected();
        }
    }

    public void giveConnection(Socket conn) {
        this.conn = conn;
    }

    public String getIP() {
        return ip;
    }
}
