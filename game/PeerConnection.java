package game;

import java.net.InetAddress;
import java.net.Socket;

public class PeerConnection {

    private InetAddress ip;
    private Socket connection;
    private PeerReceiver receiver;
    private PeerSender sender;

    public PeerConnection(InetAddress ip) {
        this.ip = ip;
    }

    public void giveConnection(Socket connection) {
        this.connection = connection;
        System.out.println("Peer connection established with: " + ip.getHostAddress());

        receiver = new PeerReceiver(this, connection);
        sender = new PeerSender(this, connection);

        receiver.start();
        sender.start();
    }

    public void receiveCommand(Command command) {
        ConnectionController.getInstance().receiveCommand(command);
    }

    public boolean isConnected() {
        return connection != null && connection.isConnected();
    }

    public InetAddress getIp() {
        return ip;
    }

    public void sendCommand(Command command) {
        sender.sendCommand(command);
    }


    public void reliefToken() {
        ConnectionController.getInstance().reliefToken();
    }

}
