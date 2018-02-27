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
import java.util.List;
import pl.polsl.SPLC.model.Person;

public class Server {

    /**
     * The port number for connecting
     */
    private final int PORT;
    
   
    /**
     * Containig persons data with they privilaged room numbers
     */
    private final List<Person> personsPrivileges;
    
    /**
     * Server's socket which is an entry point for information
     */
    private ServerSocket serverSocket;

    /**
     * The default constructor, creates a socket with the default port number
     * and informs if connection was successful
     * @param port port number
     * @param personsPrivilages list containig persons data with they privilaged room numbers
     */
    public Server(int port, List<Person> personsPrivilages) {
        this.PORT = port;
        this.personsPrivileges = personsPrivilages;
        try {
            this.serverSocket = new ServerSocket(PORT);
            System.out.println("The server is ready.");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    
    /**
     * Starts the server
     */
    public void start() {
        System.out.println("The server has started.");
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                try {
                    // creates new service for every connected client
                    SingleService singleService 
                            = new SingleService(socket, this.personsPrivileges);
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
    
    