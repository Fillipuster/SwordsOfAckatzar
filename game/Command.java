package game;

import java.util.HashMap;
import java.util.concurrent.ConcurrentMap;

public class Command {

    public enum Type {
        JOIN, MOVE, MITE; // Jeg glemmer det aldrig. MITE = POINT
    }

    public static Command decode(String cmd) {
        String[] args = cmd.split(";");
        String cmdType = args[0];
        args[0] = null;

        switch (cmdType) {
            case "JOIN":
                return new Command(Type.JOIN, args);
            case "MOVE":
                return new Command(Type.MOVE, args);
            case "MITE":
                return new Command(Type.MITE, args);
            default:
                System.out.println("Received invalid command: " + cmdType);
        }

        return null;
    }

    private Type type;
    private String[] args;

    public Command(Command.Type type, String[] args) {
        this.type = type;
        this.args = args;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(type.toString());
        for (String arg : args) {
            sb.append(';');
            sb.append(arg);
        }

        return sb.toString();
    }
}
