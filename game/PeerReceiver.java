package game;

import java.net.Socket;

public class PeerReceiver extends Thread {

    public Socket connection;

    public PeerReceiver(Socket connection) {
        this.connection = connection;
    }

    private void receive() {
        while (true);
    }

    @Override
    public void run() {
        receive();
    }

    public boolean isConnected() {
        return (connection != null) && connection.isConnected();
    }

}
