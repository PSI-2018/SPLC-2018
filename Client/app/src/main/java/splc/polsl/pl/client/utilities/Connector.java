package splc.polsl.pl.client.utilities;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.content.Context;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import android.support.v7.app.ActionBar;

import splc.polsl.pl.client.MainActivity;

public class Connector extends AsyncTask<String, Void, Void>{

    private final Context context;

    private boolean connected = false;
    private String response = null;

    Connector(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... objects) {
        try{
            String serverCommand = objects[0];
            String IP = objects[1];
            String PORT = objects[2];
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(IP,Integer.parseInt(PORT)),3000);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            out.println(serverCommand);
            this.response = in.readLine();
            this.connected = true;
            socket.close();
        }catch(Exception e){
            this.connected = false;
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(!MainActivity.isContextAvailable())
            return;
        if(this.connected){
            Toast.makeText(this.context.getApplicationContext(),this.response,Toast.LENGTH_LONG).show();
        }else{
            String message = "Could not connect to server - check connection settings";
            Toast.makeText(this.context.getApplicationContext(),message,Toast.LENGTH_LONG).show();
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