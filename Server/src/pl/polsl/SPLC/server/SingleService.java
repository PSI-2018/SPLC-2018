package pl.polsl.SPLC.server;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import pl.polsl.SPLC.protocol.Protocol;
import pl.polsl.SPLC.logger.Logger;





/**
 * A threaded service class which manages the connection between the client and
 * the server
 *
 * @author Lukasz Blasiak
 * @version 1.0
 */
public class SingleService extends Thread{

    private final String roomNumbersFileName = "cfg\\rooms.json";
    
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
    
    private final Logger logger;
    
    private final Connector connector;

    /**
     * The constructor of instance of the SingleService class. Use the socket as
     * a parameter.
     * @param socket socket representing connection to the client
     * @param connector connector with database
     * @throws java.io.IOException if there were errors with I/O stream
     */
    public SingleService(Socket socket, Connector connector) throws IOException {
        this.socket = socket;
        this.connector = connector;
        this.protocol = new Protocol();
        this.out = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream())), true);
        this.in = new BufferedReader(
                new InputStreamReader(
                        socket.getInputStream()));
        this.logger = new Logger();
    }
    
    /**
     * Overidden method. Realizes the service
     */
    @Override
    public void run() {
        try {
            this.logger.addLog("Client " + this.socket.getInetAddress().getHostAddress() + " connected");
                String clientInput = in.readLine();
                if(clientInput == null)
                    return;
                this.protocol.handleCommandLine(clientInput);
                switch(this.protocol.getCommand().toUpperCase()){
                    case "OPEN":
                        this.handleOpenDoorRequest();
                        break;
                    case "GET_DOORS_LIST":
                        if(this.handleLoginRequest(false)){
                            this.sendRoomList();
                            this.logger.addGetDoorListLog(this.socket.getInetAddress().getHostAddress(), this.protocol.getArguments());
                        }else{
                            this.logger.addLoginLog(this.socket.getInetAddress().getHostAddress(),
                                this.protocol.getArguments(),false);
                            this.out.println("AUTHORIZATION FAILED");
                        }
                        break;
                    case "LOGIN":
                        boolean loginSuccessful = this.handleLoginRequest(true);
                        this.logger.addLoginLog(this.socket.getInetAddress().getHostAddress(),
                                this.protocol.getArguments(),loginSuccessful);
                        break;
                    default:
                        out.println("COMMAND NOT FOUND");
                        break;
                }
        } catch (IOException e) {
            this.out.println(e.getMessage());
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
                this.logger.addLog("Client " + this.socket.getInetAddress().getHostAddress() + " disconnected");
            } catch (IOException exception) {
                this.logger.addLog("Exception: " + exception.getMessage());
            }
        }
    }
    
    /**
     * Method which handle open door request. 
     */
    private void handleOpenDoorRequest(){
        OpenStatus openedStatus = this.checkPrivilegesToOpenDoors();
        this.respondToClientAfterOpen(openedStatus);
        this.logger.addOpenDoorLog(this.socket.getInetAddress().getHostAddress(),
                this.protocol.getArguments(),openedStatus);
     }
    
    /**
     * Checks privileges and open selected doors
     * @return true is person has privileges, otherwise return false
     */
    private OpenStatus checkPrivilegesToOpenDoors(){
        String email = this.protocol.getArguments().get(0);
        String password = this.protocol.getArguments().get(1);  //TODO: haslo wypadalo by zahashowac (np sha256) ale dla uproszczenia zostawmy poki co tak
        String roomNumber = this.protocol.getArguments().get(2);
        if(!this.connector.login(email, password))
            return OpenStatus.AUTHORIZATION_FAILED;
        boolean isPrivileged = this.connector.checkPrivilege(email, password, roomNumber);
        OpenStatus openStatus = isPrivileged ? OpenStatus.PRIVILEGED : OpenStatus.NO_PRIVILEGED;
        return openStatus;
    }
    
    private boolean handleLoginRequest(boolean respond){
        String email = this.protocol.getArguments().get(0);
        String password = this.protocol.getArguments().get(1);
        boolean loginDataValid = this.connector.login(email, password);
        if(loginDataValid && respond)
            this.out.println("LOGGED");
        else if(respond)
            this.out.println("NOTLOGGED");
        return loginDataValid;
    }
    
          
    /**
     * Respond to client when opening the door
     * @param openedStatus status which indicates open door operation successful
     */
    private void respondToClientAfterOpen(OpenStatus openedStatus){
        if(null != openedStatus)
            switch (openedStatus) {
                case PRIVILEGED: //poprawne otwarcie drzwi
                    out.println("OPENED ROOM NR: " + this.protocol.getArguments().get(2));
                    return;
                case NO_PRIVILEGED:
                    out.println("NO PRIVILEGES TO OPEN ROOM NR: " + this.protocol.getArguments().get(2));
                    return;
                case AUTHORIZATION_FAILED:
                    out.println("AUTHORIZATION FAILED WHEN OPENING ROOM NR: " + this.protocol.getArguments().get(2));
                    return;
                case WRONG_ROOM_NUMBER:
                    out.println("WRONG ROOM NUMBER");
                    break;
                default:
                    out.println("ERROR");
                    break;
        }
    }
    
    /**
     * Sends JSON room list to client
     */
    private void sendRoomList(){
        String fileAsString = this.roomsJSONToString();
        this.out.println(fileAsString);
    }
    
    /**
     * Conver text file to String
     * @return file converted to String
     */
    private String roomsJSONToString() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(this.roomNumbersFileName));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append(" ");
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            return null;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e2) {
            }
        }
    }
}
