package pl.polsl.SPLC.server.entrypoint;
import pl.polsl.SPLC.server.Server;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;




/**
 * The starting point of server application
 * 
 * @author Lukasz Blasiak
 * @version 1.0
 */
public class ServerEntrypoint {
       
    /**
     * Name of configuration file
     */
    private final static String propertiesFileName = "cfg\\config.properties";
   
    
    /**
     * The server's entrypoint. Creates and runs it
     * @param args are user's input paraemters
     */
    public static void main(String[] args) {
        Properties prop = new Properties();
        InputStream input = null;
        int PORT;

        try {
            input = new FileInputStream(propertiesFileName);
            prop.load(input);
            PORT = Integer.parseInt(prop.getProperty("port"));

        } catch (IOException ex) {
            System.out.println("Could not open the configuration file(s)");
            return;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println("Could not open the configuration file(s)");
                    return;
                }
            }
	}

        Server server = new Server(PORT);
        server.start();
    }
}
