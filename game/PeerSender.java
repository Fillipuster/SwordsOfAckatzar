package game;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class PeerSender extends Thread {

    private PeerConnection master;
    private Socket connection;
    private DataOutputStream output;
    private String cmdStr;
    private Queue<String> commandQueue = new LinkedList<>();

    public PeerSender(PeerConnection master, Socket connection) {
        this.connection = connection;
        this.master = master;
    }

    private void setup() throws IOException {
        output = new DataOutputStream(connection.getOutputStream());
    }

    private void send() throws IOException, InterruptedException {
        sleep(10);
        if (ConnectionController.token) {
            while (!commandQueue.isEmpty()) {
                String cmd = commandQueue.poll();
                if (cmd != null && !cmd.isEmpty()) {
                    output.writeBytes(cmd);
                    output.flush();
                }
            }

            reliefToken();
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

    public void sendCommand(Command command) {
        commandQueue.add(command.toString() + "\n");
    }

    public void reliefToken() {
        master.reliefToken();
    }

}
