package game;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionController {

    // Static
    public static final int port = 6666;
    private static ConnectionController instance;

    public static ConnectionController getInstance() {
        if (instance == null) {
            instance = new ConnectionController();
        }

        return instance;
    }

    // Dynamic
    private ArrayList<Connector> connectors = new ArrayList<>();
    private ArrayList<PeerConnection> peerConnections = new ArrayList<>();

    public void addPeer(InetAddress ip) {
        PeerConnection peerConnection = new PeerConnection(ip);
        peerConnections.add(peerConnection);

        Connector connector = new Connector(ip);
        connector.start();
        connectors.add(connector);
    }

    public void handshake() {
        Handshaker handshaker = new Handshaker();
        handshaker.start();
    }

    public void addConnection(Socket connection) {
        for (Connector pc : connectors) {
            if (pc.getIp().equals(connection.getInetAddress())) {
                pc.kill();
            }
        }

        for (PeerConnection pc : peerConnections) {
            if (pc.getIp().equals(connection.getInetAddress())) {
                pc.giveConnection(connection);
            }
        }
    }

    public boolean allConnected() {
        for (PeerConnection pc : peerConnections) {
            if (!pc.isConnected()) return false;
        }

        return true;
    }

    public void broadcastCommand(Command command) {
        for (PeerConnection pc : peerConnections) {
            pc.sendCommand(command);
        }
    }

    // Commands
    public void receiveCommand(Command command) {
        switch (command.getType()) {
            case JOIN:
                Main.cmdPlayerJoin(new Player(command.getArg(0), Integer.parseInt(command.getArg(1)), Integer.parseInt(command.getArg(2)), command.getArg(3)));
                break;
            case MOVE:
                Main.cmdPlayerMove(Integer.parseInt(command.getArg(0)), Integer.parseInt(command.getArg(1)), command.getArg(2));
                break;
            default:
                System.out.println("Received unknown command.");
                break;
        }
    }

}
