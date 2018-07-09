package pl.polsl.splc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class Connector {
    private final String url = "jdbc:mysql://localhost:3306/splc";

    private final String user = "root";

    private final String password = "root";


    public boolean login(String email, String password){
        PreparedStatement stmt = null;
        Connection connection = null;
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(this.url, this.user, this.password);
            stmt = connection.prepareStatement("SELECT * from user WHERE Email=? AND Password=? AND Active=TRUE LIMIT 1");
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }finally{
            try{
                if(stmt!=null)
                    stmt.close();
                if(connection!=null)
                    connection.close();
            }catch(Exception e2){}
        }

    }

    public boolean checkPrivilege(String email, String password, String roomNumber){
        PreparedStatement stmt = null;
        Connection connection = null;
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(this.url, this.user, this.password);
            stmt = connection.prepareStatement("SELECT * from user, privilege WHERE privilege.UserId = user.Id AND user.Email=? AND user.Password=? AND user.Active=TRUE AND privilege.RoomNumber=? LIMIT 1");
            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.setString(3, roomNumber);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }finally{
            try{
                if(stmt!=null)
                    stmt.close();
                if(connection!=null)
                    connection.close();
            }catch(Exception e2){}
        }

    }
}
