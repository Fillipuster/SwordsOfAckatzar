package game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileLoader {

    private static String name;
    private static int startPosX = -1, startPosY = -1;

    public static ArrayList<String> loadClients() {
        try {
            ArrayList<String> result = new ArrayList<>();
            BufferedReader in = new BufferedReader(new FileReader("clients.txt"));
            String line = in.readLine();
            while (line != null) {
                result.add(line);
                line = in.readLine();
            }

            return result;
        } catch (IOException e) {
            System.out.println("No clients file found!");
        }

        return null;
    }

    private static void loadPlayer() {
        try {
            BufferedReader in = new BufferedReader(new FileReader("player.txt"));
            String line = in.readLine();
            while (line != null) {
                String rest = line.substring(5);
                switch (line.substring(0, 4)) {
                    case "NAME":
                        name = rest;
                        break;
                    case "XPOS":
                        startPosX = Integer.parseInt(rest);
                        break;
                    case "YPOS":
                        startPosY = Integer.parseInt(rest);
                        break;
                }

                line = in.readLine();
            }
        } catch (IOException e) {
            System.out.println("No player file found!");
        }
    }

    public static String getName() {
        if (name == null) {
            loadPlayer();
        }

        return name;
    }

    public static int getStartPosX() {
        if (startPosX < 0) {
            loadPlayer();
        }

        return startPosX;
    }

    public static int getStartPosY() {
        if (startPosY < 0) {
            loadPlayer();
        }

        return startPosY;
    }

}
