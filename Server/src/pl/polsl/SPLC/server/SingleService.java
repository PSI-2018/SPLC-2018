package pl.polsl.SPLC.server;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import pl.polsl.SPLC.model.Person;
import pl.polsl.SPLC.protocol.Protocol;



/**
 * A threaded service class which manages the connection between the client and
 * the server
 *
 * @author Lukasz Blasiak
 * @version 1.0
 */
public class SingleService extends Thread{

     /**
     * Buffered input character stream
     */
    private final BufferedReader in;
    /**
     * Formatted output character stream
     */
    private final PrintWriter out;
    /**
     * Protocol realizing the cipher
     */
    private final Protocol protocol;
    
    /**
     * Socket representing the connection
     */
    private final Socket socket;
    
    /**
     * Contain persons data and privileged room numbers
     */
    private final List<Person> personsPrivileges;

    /**
     * The constructor of instance of the SingleService class. Use the socket as
     * a parameter.
     * @param socket socket representing connection to the client
     * @param personsPrivileges contains persons data with privileged room numbers
     * @throws java.io.IOException if there were errors with I/O stream
     */
    public SingleService(Socket socket, List<Person> personsPrivileges) throws IOException {
        this.socket = socket;
        this.protocol = new Protocol();
        this.personsPrivileges = personsPrivileges;
        this.out = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream())), true);
        this.in = new BufferedReader(
                new InputStreamReader(
                        socket.getInputStream()));
    }
    
    /**
     * Method which handle open door request. 
     */
    private void handleOpenDoorRequest(){
        boolean openedSuccessful = this.checkPrivileges();
        if(openedSuccessful){
            out.println("OPENED ROOM NR: " + this.protocol.getArguments()[2]);
            System.out.println("OPENED ROOM NR: " + this.protocol.getArguments()[2]);
        }else
            out.println("NO PRIVILEGES TO OPEN ROOM NR: " + this.protocol.getArguments()[2]);
        //TODO:
        //utworzyc i dodac log (mackowski mowil, zeby zrobic tez logi)
     }
    
    /**
     * Checks privileges and open selected doors
     * @return true is person has privileges, otherwise return false
     */
    private boolean checkPrivileges(){
        String email = this.protocol.getArguments()[0];
        String password = this.protocol.getArguments()[1];  //TODO: haslo wypadalo by zahashowac (np sha256) ale dla uproszczenia zostawmy poki co tak
        int roomNumberToOpen;
        try{
            roomNumberToOpen = Integer.parseInt(this.protocol.getArguments()[2]);
        }catch(NumberFormatException e){
            System.out.println("WRONG ROOM NUMBER");
            out.println("WRONG ROOM NUMBER");
            return false;
        }
        
        for(Person person : this.personsPrivileges)
            if(person.isPrivileged(email, password, roomNumberToOpen))
                return true;
        return false;
    }
    
    /**
     * Overidden method. Realizes the service
     */
    @Override
    public void run() {
        try {
            while (true) {
                String clientInput = in.readLine();
                this.protocol.commandLineHandler(clientInput);
                switch(this.protocol.getCommand().toUpperCase()){
                    case "OPEN":
                        handleOpenDoorRequest();
                        break;
                    default:
                        out.println("COMMAND NOT FOUND");
                        break;
                }
            }
        } catch (IOException e) {
            this.out.println(e.getMessage());
        } finally {
            try {
                socket.close();
                System.out.println("The service has ended.");
            } catch (IOException exception) {
                System.err.println(exception.getMessage());
            }
        }
    }
}
