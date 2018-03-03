package splc.polsl.pl.client;
import android.os.AsyncTask;
import android.util.Log;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Connect extends AsyncTask<Void, Void, Void>{

    @Override
    protected Void doInBackground(Void... arg0) {
        try{
            Socket socket = new Socket("192.168.0.101", 1234);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            out.println("open a b 300");
            Thread.sleep(5000);
            out.println("open a b 300");
            Thread.sleep(5000);
            out.println("open a b 300");
            Thread.sleep(5000);
            out.println("open a b 300");
            Thread.sleep(5000);
        }catch(Exception e){
            String tmp = e.getMessage();
            Log.d("BLAD", tmp);
        }
        return null;
    }

}