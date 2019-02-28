package game;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class PeerConnection extends Thread {

    private String ip;
    private Socket conn;

    public PeerConnection(String ip) throws Exception {
        this.ip = ip;
    }

    private void connect() {
        try {
            System.out.println("Connecting to: " + ip);
            conn = new Socket();
            conn.connect(new InetSocketAddress(ip, 6666), 500);
        } catch (SocketTimeoutException e) {
            if (isConnected()) {
                return;
            } else {
                connect();
            }
        } catch (IOException e) {}
    }

    @Override
    public void run() {
        connect();

        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            DataOutputStream output = new DataOutputStream(conn.getOutputStream());
            while (true) {
                output.writeBytes(Main.name + "\n");
                String str = input.readLine();
                if (str != null) {
                    System.out.println(conn.getInetAddress().toString() + " is called: " + str);
                }
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            System.out.println("AIDS");
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
