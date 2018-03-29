package pl.polsl.SPLC.model;

import java.util.ArrayList;
import java.util.List;
import pl.polsl.SPLC.server.OpenStatus;


/**
 * Contains single person data and privileges
 * @author Lukasz
 */
public class Person {
    private final String email;
    private final String password;
    private final List<Integer> privilegedRooms;
    
    /**
     * Default constructor
     * @param email person email
     * @param password person password
     */
    public Person(String email, String password){
        this.email = email;
        this.password = password;
        this.privilegedRooms = new ArrayList<>();
    }
    
    /**
     * Adds new privileged room number
     * @param newPrivilegedRoomNumber new privileged room number
     */
    public void addPrivilegedRoom(int newPrivilegedRoomNumber){
        for(int privilegedRoomNumber : privilegedRooms)
            if(privilegedRoomNumber == newPrivilegedRoomNumber)
                return;
        privilegedRooms.add(newPrivilegedRoomNumber);
    }

    /**
     * Check if person has privileges to open selected door
     * @param email person email passed by client application
     * @param password person password passed by client application
     * @param roomNumberToOpen person room number passed by client application
     * @return true if has privilege, otherwise return false
     */
    public OpenStatus isPrivileged(String email, String password, int roomNumberToOpen){
        if(!this.email.equals(email) || !this.password.equals(password))
            return OpenStatus.AUTHORIZATION_FAILED;
        for(int privilegedRoom : this.privilegedRooms)
            if(privilegedRoom == roomNumberToOpen)
                return OpenStatus.PRIVILEGED;
        return OpenStatus.NO_PRIVILEGED;
    }
    
    public boolean isLoginDataCorrect(String email, String password){
        return (this.email.equals(email) && this.password.equals(password));
    }
    
    /**
     * Print user data
     * @return user data
     */
    @Override
    public String toString() {
        return "Person{" + "email=" + email + ", password=" + password + '}';
    }
}
