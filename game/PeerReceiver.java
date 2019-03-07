package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class PeerReceiver extends Thread {

    private Socket connection;

    public PeerReceiver(Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        super.run();
    }
}
