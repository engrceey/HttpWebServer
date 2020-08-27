import model.Server;

import java.io.IOException;

public class main {
    public static void main(String[] args) {
//        Entry point into program

        int port = 8080;

//        Instantiates the server and opens up a port at 8080
        try {
            Server serverThread = new Server(port);
            serverThread.start();
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }
}
