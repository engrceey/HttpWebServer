package model;

import util.ServerEngine;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{

    //    Field and constructor to create server connection
    private final ServerSocket serverSocket;
    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
//        Print Statement for easy navigation to browser
        System.out.println("click here: 'http://localhost:8080/' ");

//        Open socket on port 8080 with an infinity loop to pause the server
        while (serverSocket.isBound() && !serverSocket.isClosed()) {
            try {
                Socket socket = serverSocket.accept();
                ServerEngine serverEngine = new ServerEngine(socket);
//                blocking .start() method to connection from client
                serverEngine.start();
            }catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }
}
