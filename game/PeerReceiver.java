package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.Buffer;

public class PeerReceiver extends Thread {

    private Socket connection;
    private BufferedReader input;

    public PeerReceiver(Socket connection) {
        this.connection = connection;
    }

    private void setup() throws IOException {
        input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    }

    private void receive() throws IOException {
        String in = input.readLine();
        System.out.println(String.format("%s says: %s", connection.getInetAddress().getHostAddress(), in));

        receive(); // Recursive call.
    }

    @Override
    public void run() {
        try {
            setup();
            receive();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
