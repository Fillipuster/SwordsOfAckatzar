package game;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.Queue;

public class PeerSender extends Thread {

    private Socket connection;
    private DataOutputStream output;
    private String cmdStr;

    public PeerSender(Socket connection) {
        this.connection = connection;
    }

    private void setup() throws IOException {
        output = new DataOutputStream(connection.getOutputStream());
    }

    private void send() throws IOException, InterruptedException {
        if (!cmdStr.isEmpty()) {
            output.writeBytes(cmdStr + "\n");
            output.flush();
        }

        send(); // Recursive call.
    }

    @Override
    public void run() {
        try {
            setup();
            send();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void queueCmd(String cmd) {
        cmdStr = cmd;
    }

}
