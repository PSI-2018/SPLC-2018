package pl.polsl.SPLC.server.entrypoint;
import java.io.BufferedReader;
import pl.polsl.SPLC.server.Server;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import pl.polsl.SPLC.model.Person;

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
    private final static String propertiesFileName = "config.properties";
    /**
     * Name of file containing privileges
     */
    private final static String privilegesFileName = "privileges.txt";
    
    /**
     * Contains persons data and privileges
     */
    private final static List<Person> personsPrivileges = new ArrayList<>();
    
    /**
     * Load privileges from text file
     * @throws IOException occur when failed open file
     */
    private static void loadPrivileges() throws IOException{
        FileReader fileReader = new FileReader(privilegesFileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String singleLine;
        while((singleLine = bufferedReader.readLine()) != null) {
                ServerEntrypoint.parseSingleLine(singleLine);
            } 
    }
    
    /**
     * Parse single line readed from permisions file
     * @param singleLine single line readed from permisions file
     */
    private static void parseSingleLine(String singleLine){
        if(singleLine == null || singleLine.isEmpty())
            return;
        List<String> inputs = new ArrayList<>(Arrays.asList(singleLine.split(" ")));
        if(inputs.size() < 3)
            return;
        String email = inputs.get(0);
        String password = inputs.get(1);
        List<Integer> privilegedRooms = new ArrayList<>();
        for(int i=2; i<inputs.size(); i++)
            try{
                int privilegedRoom = Integer.parseInt(inputs.get(i));
                privilegedRooms.add(privilegedRoom);
            }catch(NumberFormatException e){}
        addNewPerson(email, password, privilegedRooms);
    }
    
    /**
     * Adds new person to permisions list
     * @param email person email
     * @param password person password
     * @param privilegedRooms list of privileged rooms
     */
    private static void addNewPerson(String email, String password, List<Integer> privilegedRooms){
        if(email == null || password==null || privilegedRooms == null)
            return;
        Person person = new Person(email, password);    
        for(int privilegedRoom : privilegedRooms)
            person.addPrivilegedRoom(privilegedRoom);
        personsPrivileges.add(person);
    }
    
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
            ServerEntrypoint.loadPrivileges();

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

        Server server = new Server(PORT, personsPrivileges);
        server.start();
    }
}
