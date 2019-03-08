package game;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ConnectionController {

    // Static
    public static final int port = 6666;
    private static ConnectionController instance;
    public static boolean token;

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

    public void startTokenPassing() {
        if (getAddressIndex() == 0) {
            System.out.println("YOU HAVE THE TOKEN!");
            token = true;
        }
    }

    public void broadcastCommand(Command command) {
        for (PeerConnection pc : peerConnections) {
            pc.sendCommand(command);
        }
    }

    // Commands
    public void receiveCommand(Command command) {
        switch (command.getType()) {
            case TOKN:
                System.out.println("TOKEN");
                token = true;
                break;
            case JOIN:
                Main.cmdPlayerJoin(new Player(command.getArg(0), Integer.parseInt(command.getArg(1)), Integer.parseInt(command.getArg(2)), command.getArg(3)));
                break;
            case MOVE:
                Main.cmdPlayerMove(Integer.parseInt(command.getArg(0)), Integer.parseInt(command.getArg(1)), command.getArg(2), Integer.parseInt(command.getArg(3)));
                break;
            case MITE:
                Main.cmdPlayerScore(Integer.parseInt(command.getArg(0)), Integer.parseInt(command.getArg(1)), Integer.parseInt(command.getArg(2)));
            default:
                System.out.println("Received unknown command: " + command.toString());
                break;
        }
    }

    private int getAddressIndex() {
        int result = -1;

        try {
            String myIp = InetAddress.getLocalHost().getHostAddress();

            for (int i = 0; i < Main.playerAddresses.length; i++) {
                if (Main.playerAddresses[i].equalsIgnoreCase(myIp)) {
                    result = i;
                    break;
                }
            }

        } catch (UnknownHostException e) {
            System.out.println("Failed to get local host address.");
        }

        return result;
    }

    public void reliefToken() {
        int myAddress = getAddressIndex();
        int target = myAddress + 1;
        if (target >= Main.playerAddresses.length) target = 0;

        if (myAddress >= 0) {
            for (PeerConnection pc : peerConnections) {
                if (pc.getIp().getHostAddress().equalsIgnoreCase(Main.playerAddresses[target])) pc.sendCommand(new Command(CMDT.TOKN, new String[]{}));
            }
        }
    }

}
