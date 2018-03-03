package pl.polsl.SPLC.server;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    private final String logFileName = "logs.log";
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
        OpenStatus openedSuccessful = this.checkPrivileges();
        String singleLogRecord = createSingleLogRecord(openedSuccessful);
        updateLogFile(singleLogRecord);
     }
    
    /**
     * Checks privileges and open selected doors
     * @return true is person has privileges, otherwise return false
     */
    private OpenStatus checkPrivileges(){
        String email = this.protocol.getArguments()[0];
        String password = this.protocol.getArguments()[1];  //TODO: haslo wypadalo by zahashowac (np sha256) ale dla uproszczenia zostawmy poki co tak
        int roomNumberToOpen;
        try{
            roomNumberToOpen = Integer.parseInt(this.protocol.getArguments()[2]);
        }catch(NumberFormatException e){
            return OpenStatus.WRONG_ROOM_NUMBER;
        }
        OpenStatus openStatus;
        for(Person person : this.personsPrivileges){
            openStatus = person.isPrivileged(email, password, roomNumberToOpen);
            if(openStatus == OpenStatus.PRIVILEGED || openStatus == OpenStatus.NO_PRIVILEGED)
                return openStatus;
        }
            
        return OpenStatus.AUTHORIZATION_FAILED;
    }
    
    /**
     * Create single log record
     * @param openedStatus status from opening selected doors
     * @return new single log record to add to main log file
     */
    private String createSingleLogRecord(OpenStatus openedStatus){
        String singleLogRecord = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime())
                + " by: " + this.protocol.getArguments()[0]
                + " IP: " + this.socket.getInetAddress().getHostAddress(); 
        if(null != openedStatus)
            switch (openedStatus) {
                case PRIVILEGED: //poprawne otwarcie drzwi
                    singleLogRecord+= " OPENED ROOM NR: " + this.protocol.getArguments()[2];
                    out.println("OPENED ROOM NR: " + this.protocol.getArguments()[2]);
                    System.out.println("OPENED ROOM NR: " + this.protocol.getArguments()[2]);
                    break;
                case NO_PRIVILEGED:
                    singleLogRecord+= " NO PRIVILEGES TO OPEN ROOM NR: " + this.protocol.getArguments()[2];
                    System.out.println("NO PRIVILEGES TO OPEN ROOM NR: " + this.protocol.getArguments()[2]);
                    out.println("NO PRIVILEGES TO OPEN ROOM NR: " + this.protocol.getArguments()[2]);
                    break;
                case AUTHORIZATION_FAILED:
                    singleLogRecord+= " AUTHORIZATION FAILED WHEN OPENING ROOM NR: " + this.protocol.getArguments()[2];
                    System.out.println("AUTHORIZATION FAILED WHEN OPENING ROOM NR: " + this.protocol.getArguments()[2]);
                    out.println("AUTHORIZATION FAILED WHEN OPENING ROOM NR: " + this.protocol.getArguments()[2]);
                    break;
                case WRONG_ROOM_NUMBER:
                    singleLogRecord+= " WRONG ROOM NUMBER: " + this.protocol.getArguments()[2];
                    System.out.println("WRONG ROOM NUMBER");
                    out.println("WRONG ROOM NUMBER");
                    break;
                default:
                    break;
        }
        return singleLogRecord;    
    }
    
    /**
     * Update main log file
     * @param singleLogRecord new single log record 
     */
    public void updateLogFile(String singleLogRecord){
        FileWriter fstream = null;
        BufferedWriter fbw = null;
        try{
            fstream = new FileWriter(this.logFileName,true);
            fbw = new BufferedWriter(fstream);
            fbw.write(singleLogRecord);
            fbw.newLine();
        }catch(IOException ex){
        }finally{
            try{
                if(fbw!=null)
                    fbw.close();
                if(fstream!=null)
                    fstream.close();
            }catch(IOException e){}
        }
    }
    
    /**
     * Overidden method. Realizes the service
     */
    @Override
    public void run() {
        try {
            System.out.println("Client connected");
            while (true) {
                String clientInput = in.readLine();
                if(clientInput == null)
                    break;
                this.protocol.handleCommandLine(clientInput);
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
                System.out.println("Client disconnected");
            } catch (IOException exception) {
                System.err.println(exception.getMessage());
            }
        }
    }
}
