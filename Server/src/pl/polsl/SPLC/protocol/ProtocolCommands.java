package pl.polsl.SPLC.protocol;

/**
 * Enum which constains protocol commands
 *
 * @author Lukasz Blasiak
 * @version 1.0
 */



public enum ProtocolCommands {
    /**
     * Open doors command
     */
    OPEN,
    /**
     * Returns doors list
     */
    GET_DOORS_LIST,
    
    /**
     * Returns login operation successful
     */
    LOGIN
}
