package polsl.splc;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoggedActivity extends AppCompatActivity {
    private Button checkBtn, restartBtn;
    private TextView rightsText;
    private Spinner floors;
    private Spinner rooms;
    int chosenRoom, chosenFloor;
    List<Floor> allFloors;
    String userEmail, userPassword;
    ClientCommunication client;
    String port, host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        allFloors = new ArrayList<Floor>();
        checkBtn = findViewById(R.id.entitlementsButton);
        restartBtn = findViewById(R.id.restartButton);
        rightsText = findViewById(R.id.noRightsText);
        floors = findViewById(R.id.floorSpinner);
        rooms = findViewById(R.id.roomSpinner);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userEmail = bundle.getString("email");
            userPassword = bundle.getString("password");
            port = bundle.getString("port");
            host = bundle.getString("host");
        }
        try {
            client = new ClientCommunication(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            getRoomsFromServer();
            String[] allFloorNames = new String[allFloors.size()];

            for (int i = 0; i < allFloors.size(); i++) {
                String singleName = allFloors.get(i).getFloorName();
                allFloorNames[i] = singleName;
            }
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, allFloorNames);
            floors.setAdapter(adapter);
            floors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int id, long position) {
                    manageRooms(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
            rooms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int id, long position) {
                    chosenRoom = (int) position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFromSpinners();
            }

        });
        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = getBaseContext().getPackageManager().
                        getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }

        });
    }

    void manageRooms(long position) {
        chosenFloor = (int) position;
        String[] allRoomNames = allFloors.get(chosenFloor).getAllRoomsNames();
        ArrayAdapter roomAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, allRoomNames);
        rooms.setAdapter(roomAdapter);
        roomAdapter.notifyDataSetChanged();
    }

    void getRoomsFromServer() throws JSONException {
        String all = client.getDoorList(userEmail, userPassword);
         JSONObject mainObj = new JSONObject(all);
        if (mainObj != null) {
            JSONArray list = mainObj.getJSONArray("floors");
            if (list != null) {
                for (int i = 0; i < list.length(); i++) {
                    JSONObject elem = list.getJSONObject(i);
                    if (elem != null) {
                        String floorName = elem.getString("name");
                        Floor newFloor = new Floor();
                        newFloor.setFloorName(floorName);
                        JSONArray roomNrs = elem.getJSONArray("room_numbers");
                        if (roomNrs != null) {
                            for (int j = 0; j < roomNrs.length(); j++) {
                                String roomName = roomNrs.getString(j);
                                newFloor.addRoom(roomName);
                            }
                        }
                        allFloors.add(newFloor);
                    }
                }

            }
        }

    }

    void getFromSpinners() {
        /*
        rooms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int id, long position) {
                chosenRoom = (int) position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });*/
        int roomNumber = allFloors.get(chosenFloor).getAllRooms().get(chosenRoom).getNumber();
        boolean ifOk = client.checkPrivileges(userEmail, userPassword, Integer.toString(roomNumber));
        if (ifOk) {
            rightsText.setVisibility(View.VISIBLE);
            rightsText.setText("Room " + roomNumber + " was succesfully opened");
        } else {
            rightsText.setVisibility(View.VISIBLE);
        }
    }
}
