package polsl.splc;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Class with communication protocol for client
 *
 * @author Katarzyna Urbasik
 * @version 1.0
 */
public class ClientCommunication extends MainActivity {
    String emailGiven, passwordGiven, doorList;
    String operationStringResult="";
    boolean exOk = false, finnished = false;

   String host, portString;

    public ClientCommunication(String givenHost, String givenPort) throws IOException {
        host = givenHost;
        portString = givenPort;
    }

    public boolean checkLogin(String userEmail, String userPassword) throws IOException {
        emailGiven = userEmail;
        passwordGiven = userPassword;

        new AsyncAction().execute(host, portString, "1", emailGiven, passwordGiven);
        while (!finnished) {
         }
        ;

        finnished = false;

        if (exOk) {
            return true;
        } else {
             return false;
        }
    }

    public String getDoorList(String mail, String password) {
        new AsyncAction().execute(host, portString, "2", mail, password);
        while (!finnished) {
        }
        ;
        finnished = false;
        if (exOk) {
            return doorList;
        } else {
            return "problem";
        }
    }

    public boolean checkPrivileges(String userEmail, String userPassword, String roomNumber) {
        new AsyncAction().execute(host, portString, "3", userEmail, userPassword, roomNumber);
        while (!finnished) {
        }
        ;
        exOk=false;
        while(!exOk){};
        finnished = false;
        if (operationStringResult.startsWith("OPENED")) {
            return true;
        } else {
            return false;
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    public class AsyncAction extends AsyncTask<String, Void, String> {
        String hostA;
        int portA;
        Socket socket;
        PrintWriter outT;
        BufferedReader in;

        @Override
        protected String doInBackground(String... args) {
            hostA = args[0];
            portA = Integer.parseInt(args[1]);

            int option = Integer.parseInt(args[2]);
            switch (option) {
                case 1: //login
                   if (loginUser(args[3], args[4])) {
                        ClientCommunication.this.exOk = true;
                    }
                    ;
                    break;
                case 2: //get list
                     if (getRoomList(args[3], args[4])) {
                        ClientCommunication.this.exOk = true;
                    }
                    break;
                case 3: //check privileges
                    userPrivileges(args[3], args[4], args[5]);
                break;
            }
            ClientCommunication.this.finnished = true;
            return null;
        }

        void userPrivileges(String userEmail, String userPassword, String roomNumber) {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(hostA, portA), 3000);
                outT = new PrintWriter(
                        new BufferedWriter(
                                new OutputStreamWriter(
                                        socket.getOutputStream())), true);

                in = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()));

                String command = "OPEN" + " " + userEmail + " " + userPassword + " " + roomNumber;
                outT.println(command);
                String result = in.readLine();
                 socket.close();
                ClientCommunication.this.operationStringResult=result;
                ClientCommunication.this.exOk = true;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        boolean getRoomList(String userMail, String userPassword) {
            boolean operationResult = false;
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(hostA, portA), 3000);
                outT = new PrintWriter(
                        new BufferedWriter(
                                new OutputStreamWriter(
                                        socket.getOutputStream())), true);

                in = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()));

                String command = "GET_DOORS_LIST" + " " + userMail + " " + userPassword;
                outT.println(command);
                String result = in.readLine();
                socket.close();
                if (!(result.equals("AUTHORIZATION FAILED") || result.equals("COMMAND NOT FOUND"))) {
                    ClientCommunication.this.doorList = result;
                    operationResult = true;
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return operationResult;
        }

        boolean loginUser(String userMail, String userPassword) {
            boolean operationResult = false;
            try {
                socket = new Socket();
                System.out.println("host: "+hostA+" port: "+portA);
                socket.connect(new InetSocketAddress(hostA, portA), 3000);
                outT = new PrintWriter(
                        new BufferedWriter(
                                new OutputStreamWriter(
                                        socket.getOutputStream())), true);

                in = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()));

                String command = "LOGIN" + " " + userMail + " " + userPassword;

                outT.println(command);
                String result = in.readLine();

                socket.close();
                if (result.equals("LOGGED")) {
                  operationResult = true;
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return operationResult;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ClientCommunication.this.finnished = true;
        }
    }

}
