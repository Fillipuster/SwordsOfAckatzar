package game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Handshaker extends Thread {

    private ServerSocket server;

    private void createServer() throws IOException {
        server = new ServerSocket(ConnectionController.port);
        server.setSoTimeout(5000);
    }

    private void handshake() throws IOException {
        try {
            Socket connection = server.accept();
            ConnectionController.getInstance().addConnection(connection);
        } catch (SocketTimeoutException e) {}

        if (!ConnectionController.getInstance().allConnected()) {
            handshake();
        }

        System.out.println("All connections established. Stopping handshaker.");
    }

    @Override
    public void run() {
        try {
            createServer();
            handshake();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}