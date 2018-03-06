package splc.polsl.pl.client.utilities;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.content.Context;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import splc.polsl.pl.client.MainActivity;

public class Connector extends AsyncTask<Object, Void, Void>{

    private final Context context;

    private boolean connected = false;
    private String serverResponse = null;
    private RequestType requestType;

    public Connector(Context context) {
        this.context = context;
    }

    public enum RequestType{
        OPEN_DOOR,
        TEST_CONNECTION
    }


    @Override
    protected Void doInBackground(Object... objects) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String serverIP = preferences.getString("serverIP","192.168.0.1");
        Integer port = preferences.getInt("port",1234);
        this.requestType = (RequestType)objects[0];
        switch(this.requestType){
            case TEST_CONNECTION:
                this.testConnection(serverIP,port);
                break;
            case OPEN_DOOR:
                String serverCommand = (String)objects[1];
                this.sendOpenDoorRequest(serverIP,port.toString(),serverCommand);
                break;
        }
        return null;
    }

    private void testConnection(String serverIP, Integer port){
        Socket socket = new Socket();
        try{
            socket.connect(new InetSocketAddress(serverIP,port),3000);
            this.connected = socket.isConnected();
        }catch (Exception e){
            this.connected = false;
        } finally{
            try{
                socket.close();
            }catch(IOException e){}
        }
    }

    private void sendOpenDoorRequest(String serverIP, String port, String serverCommand){
        try{

            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(serverIP,Integer.parseInt(port)),3000);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            out.println(serverCommand);
            this.serverResponse = in.readLine();
            this.connected = true;
            socket.close();
        }catch(Exception e){
            this.connected = false;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        String message;
        if(!MainActivity.isContextAvailable())
            return;
        switch(this.requestType){
            case TEST_CONNECTION:
                message = this.connected ? "Connection successful" : "Could not connect to server - check connection settings";
                Toast.makeText(this.context,message,Toast.LENGTH_SHORT).show();
                break;
            case OPEN_DOOR:
                if(!MainActivity.isContextAvailable())
                    return;
                if(this.connected)
                    Toast.makeText(this.context.getApplicationContext(),this.serverResponse,Toast.LENGTH_SHORT).show();
                else{
                    message = "Could not connect to server - check connection settings";
                    Toast.makeText(this.context.getApplicationContext(),message,Toast.LENGTH_LONG).show();
                }
                break;
        }


    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Void aVoid) {
        super.onCancelled(aVoid);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

}