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
    private final List<String> arguments;
    /**
     * Variable which contains client command
     */
    private String command ;
    
    /**
     * Amount of arguments for 'open' command
     */
    private final int OPEN_COMMAND_ARGUMENTS_COUNT = 3;
    
    /**
     * Amount of arguments for 'get_door_list' command
     */
    private final int DOORS_LIST_COMMAND_ARGUMENTS_COUNT = 2;
    
    /**
     * Amount of arguments for 'login' command
     */
    private final int LOGIN_COMMAND_ARGUMENTS_COUNT = 2;
    
    /**
     * Default constructor for protocol
     */
    public Protocol(){
     this.command = "UNKNOWN COMMAND";  
     this.arguments = new ArrayList<>();
    }
    
    
    /**
     * Method which check if command is correct
     * @param clientInputLine client input line
     */
    public void handleCommandLine(String clientInputLine){
        if(clientInputLine == null){
            this.command = "UNKNOWN COMMAND";
            this.arguments.clear();
            return;
        }
        List<String> inputs = new ArrayList<>(Arrays.asList(clientInputLine.split(" ")));
        if(inputs.isEmpty() || !this.isCorrectCommand(inputs.get(0)) || !isArgumentsAmountCorrent(inputs)){
            this.command = "UNKNOWN COMMAND";
            this.arguments.clear();
        }
        else{
            this.command = inputs.get(0);
            for(int i=1; i<inputs.size();i++)
                this.arguments.add(inputs.get(i));
        }
    }
    
    /**
     * Check if arguments amount is correct for command
     * @param inputs user inputs
     * @return true if amount if correct, otherwise return false
     */
    private boolean isArgumentsAmountCorrent(List<String> inputs){
        switch(inputs.get(0).toUpperCase()){
            case "OPEN":
                return inputs.size() == this.OPEN_COMMAND_ARGUMENTS_COUNT + 1;
            case "GET_DOORS_LIST":
                return inputs.size() == this.DOORS_LIST_COMMAND_ARGUMENTS_COUNT + 1;
            case "LOGIN":
                return inputs.size() == this.LOGIN_COMMAND_ARGUMENTS_COUNT + 1;
        }
        return false;
    }
    
    /**
     * Getter for client arguments
     * @return array of client arguments
     */
    public List<String> getArguments(){
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
