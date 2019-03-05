package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class PeerReceiver extends Thread {

    public Socket connection;

    public PeerReceiver(Socket connection) {
        this.connection = connection;
    }

    private void receive() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while (true) {
                String str = input.readLine();
                Main.receiveCommand(connection.getInetAddress(), Command.decode(str));
            }
        } catch (IOException e) {}
    }

    @Override
    public void run() {
        receive();
    }

    public boolean isConnected() {
        return (connection != null) && connection.isConnected();
    }

}
