package game;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Connector extends Thread {

    private InetAddress ip;
    private boolean kill = false;

    public Connector(InetAddress ip) {
        this.ip = ip;
    }

    private void connect() throws IOException {
        try {
            Socket connection = new Socket();
            connection.connect(new InetSocketAddress(ip, 6666), 1000);
            ConnectionController.getInstance().addConnection(connection);
        } catch (SocketTimeoutException e) {
            if (!kill) {
                connect();
            }
        }
    }

    @Override
    public void run() {
        try {
            connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InetAddress getIp() {
        return ip;
    }

    public void kill() {
        kill = true;
    }

}
