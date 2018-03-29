/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.SPLC.logger;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import pl.polsl.SPLC.server.OpenStatus;

/**
 *
 * @author Lukasz
 */
public class Logger {
    
    private final String logFileName = "cfg\\logs.log";
    
    private String createLogHeader(String IP, String user){
        String singleLog = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime())
                + " by: " + user
                + " IP: " + IP
                + "\t";
        return singleLog;
    }
    
    public void addOpenDoorLog(String IP, List<String> arguments, OpenStatus openStatus){
        String singleLog = this.createLogHeader(IP, arguments.get(0));
        singleLog += this.createOpenRoomLogRecord(openStatus, arguments);
        this.updateLogFile(singleLog);
    }
    
    /**
     * Create single log record
     * @param openedStatus status from opening selected doors
     * @return new single log record to add to main log file
     */
    private String createOpenRoomLogRecord(OpenStatus openedStatus, List<String> arguments){
        String singleLogRecord = null;
        if(null != openedStatus)
            switch (openedStatus) {
                case PRIVILEGED: //poprawne otwarcie drzwi
                    singleLogRecord = "OPENED ROOM NR: " + arguments.get(2);
                    break;
                case NO_PRIVILEGED:
                    singleLogRecord = "NO PRIVILEGES TO OPEN ROOM NR: " + arguments.get(2);
                    break;
                case AUTHORIZATION_FAILED:
                    singleLogRecord = "AUTHORIZATION FAILED WHEN OPENING ROOM NR: " + arguments.get(2);
                    break;
                case WRONG_ROOM_NUMBER:
                    singleLogRecord = "WRONG ROOM NUMBER: " + arguments.get(2);
                    break;
                default:
                    break;
        }
        return singleLogRecord;    
    }
    
    
    public void addLog(String singleLog){
        if(singleLog==null)
            return;
        String header = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
        singleLog = header + "\t" + singleLog;
        this.updateLogFile(singleLog);
    }
    
    
    public void addGetDoorListLog(String IP, List<String> arguments){
        String singleLogRecord = this.createLogHeader(IP, arguments.get(0));
        singleLogRecord += "Sent rooms list";
        this.updateLogFile(singleLogRecord);
    }
    
    public void addLoginLog(String IP, List<String> arguments, boolean loginSuccessful){
        String singleLogRecord = this.createLogHeader(IP, arguments.get(0));
        singleLogRecord += loginSuccessful ? "Login successful" : "Authorization failed";
        this.updateLogFile(singleLogRecord);
    }
    
    /**
     * Update main log file
     * @param singleLogRecord new single log record 
     */
    private void updateLogFile(String singleLogRecord){
        System.out.println(singleLogRecord);
        FileWriter fstream = null;
        BufferedWriter fbw = null;
        try{
            fstream = new FileWriter(this.logFileName,true);
            fbw = new BufferedWriter(fstream);
            fbw.write(singleLogRecord);
            fbw.newLine();
        }catch(IOException ex){
        }finally{
            try{
                if(fbw!=null)
                    fbw.close();
                if(fstream!=null)
                    fstream.close();
            }catch(IOException e){}
        }
    }
}
