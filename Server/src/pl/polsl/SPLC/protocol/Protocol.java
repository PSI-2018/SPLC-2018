package pl.polsl.SPLC.protocol;
import java.util.*;

/**
 * Protocol class which handle user commands send to the server. It parse the 
 * input line, set actual status and arguments. It also validate commands
 * @author Lukasz Blasiak
 * @version 1.0
 */
public class Protocol {
    
    
    /**
     * String array which contains client arguments
     */
    private final String[] arguments;
    /**
     * Variable which contains client command
     */
    private String command ;
    
    /**
     * Amount of arguments
     */
    private final int argumentsCount = 3;
    
    /**
     * Default constructor for protocol
     */
    public Protocol(){
     this.command = "UNKNOWN COMMAND";  
     this.arguments = new String[argumentsCount];
    }
    
    
    /**
     * Method which check if command is correct
     * @param clientInputLine client input line
     */
    public void handleCommandLine(String clientInputLine){
        if(clientInputLine == null){
            this.command = "UNKNOWN COMMAND";
            return;
        }
        List<String> inputs = new ArrayList<>(Arrays.asList(clientInputLine.split(" ")));
        if(inputs.isEmpty() || !this.isCorrectCommand(inputs.get(0)) || inputs.size() != this.argumentsCount+1)
            this.command = "UNKNOWN COMMAND";
        else{
            this.command = inputs.get(0);
            for(int i=1; i<argumentsCount+1;i++)
                this.arguments[i-1] = inputs.get(i);
        }
    }
    
    /**
     * Getter for client arguments
     * @return array of client arguments
     */
    public String[] getArguments(){
        return this.arguments;
    }
    
    /**
     * Getter for client command
     * @return client command
     */
    public String getCommand(){
        return this.command;
    }
    
    /**
     * Method which check if command is correct
     * @param command client input command
     * @return return true if command is correct, otherwise return false 
     */
    private boolean isCorrectCommand(String command) {
        for (ProtocolCommands protocolCommand : ProtocolCommands.values())
            if (command.equalsIgnoreCase(protocolCommand.name()))
                return true;
        return false;
    }
}
