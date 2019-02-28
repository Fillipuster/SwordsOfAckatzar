package game;

import java.io.BufferedReader;
import java.io.IOException;

public class Commander extends Thread {

    private BufferedReader in;

    public Commander(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String line = in.readLine();
                switch (line) {
                    case "ready":
                        ready();
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                System.out.println("Klamydia.");
            }

        }
    }

    private void ready() {
        //Main.listen();
    }
}
