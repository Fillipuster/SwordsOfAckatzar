package game;

import java.util.HashMap;
import java.util.concurrent.ConcurrentMap;

public class Command {

    private static CMDT[] arrTypes = {CMDT.JOIN, CMDT.MOVE, CMDT.MITE};
    private static String[] arrStrings = {"JOIN", "MOVE", "MITE"};

    public static Command decode(String cmd) {
        String[] split = cmd.split(";");
        String typeString = split[0];
        CMDT typeEnum = CMDT.EROR;

        String[] args = new String[split.length - 1];
        for (int i = 0; i < args.length; i++) {
            args[i] = split[i + 1];
        }

        for (int i = 0; i < arrTypes.length; i++) {
            if (typeString.equalsIgnoreCase(arrStrings[i])) {
                typeEnum = arrTypes[i];
            }
        }

        return new Command(typeEnum, args);
    }

    private CMDT type;
    private String[] args;

    public Command(CMDT type, String[] args) {
        this.type = type;
        this.args = args;
    }

    public CMDT getType() {
        return type;
    }

    public String getArg(int index) {
        return args[index];
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
