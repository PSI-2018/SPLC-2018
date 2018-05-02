package pl.polsl.SPLC.server;

/** 
 * Main server class. It configure port, create models and create single service
 * for clients.
 * @author Lukasz Blasiak
 * @version 1.0
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    /**
     * The port number for connecting
     */
    private final int PORT;
   
    /**
     * Server's socket which is an entry point for information
     */
    private ServerSocket serverSocket;

    private Connector connector = new Connector();
    
    /**
     * The default constructor, creates a socket with the default port number
     * and informs if connection was successful
     * @param port port number
     */
    public Server(int port) {
        this.PORT = port;
        try {
            this.serverSocket = new ServerSocket(PORT);
            System.out.println("The server has been configured properly.");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    
    /**
     * Starts the server
     */
    public void start() {
        try {
            System.out.println("The server has started.");
            while (true) {
                Socket socket = serverSocket.accept();
                try {
                    // creates new service for every connected client
                    SingleService singleService 
                            = new SingleService(socket, this.connector);
                    singleService.start();
                } catch (IOException e) {
                    socket.close();
                    System.err.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
    
    