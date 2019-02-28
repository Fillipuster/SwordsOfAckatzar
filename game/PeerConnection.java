package game;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class PeerConnection extends Thread {

    private String ip;
    private Socket conn;

    public PeerConnection(Socket conn) throws Exception {
        this.conn = conn;
        System.out.println("Connected to: " + conn.getInetAddress());
    }

    @Override
    public void run() {
//        try {
//            connect();
//        } catch (IOException e) {
//            System.out.println("IOException for peer thread: " + ip);
//            e.printStackTrace();
//        }
//
//        // We have connection.
//        logic();
    }

    private void logic() {

    }

    private void connect() throws IOException {
        ServerSocket handshaker = new ServerSocket(6666);
        try {
            conn = new Socket(ip, 6666);
        } catch (ConnectException e) {
            conn = handshaker.accept();
        }

        System.out.println("Connection?: " + conn.getInetAddress());
    }
}
