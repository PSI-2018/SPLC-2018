package polsl.splc;

import java.util.ArrayList;
import java.util.List;



public class Floor {
    String name;
    List<Room> allRooms;

    Floor() {
        name = "";
        allRooms = new ArrayList<Room>();
    }

    void setFloorName(String newName) {
        name = newName;
    }

    String getFloorName() {
        return name;
    }

    List<Room> getAllRooms() {
        return allRooms;
    }

    String[] getAllRoomsNames() {
        String[] names = new String[allRooms.size()];
        for (int i = 0; i < allRooms.size(); i++) {
            names[i] = Integer.toString(allRooms.get(i).getNumber());
        }
        return names;
    }

    void addRoom(String roomName) {

        int roomNumber = Integer.parseInt(roomName);
        Room newRoom = new Room();
        newRoom.setNumber(roomNumber);
        allRooms.add(newRoom);
    }
}

