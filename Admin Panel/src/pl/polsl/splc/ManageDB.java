package pl.polsl.splc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ManageDB {
    public static Boolean postUser(String email, String password) throws Exception {
        int admin, active;
        admin = 0;
        active = 1;
        try {
//            Check for privilege duplication
            Connection con = getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("SELECT count(*) AS total FROM user WHERE Email = '" + email + "';");
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            int result = rs.getInt("total");

            if (result > 0) {
                preparedStatement = con.prepareStatement("DELETE FROM user WHERE Email = '" + email + "';");
                preparedStatement.executeUpdate();
            }
            preparedStatement = con.prepareStatement("INSERT INTO user (Email, Password, IsAdmin, Active) VALUES ('" + email + "', '" + password + "', '" + admin + "', '" + active + "')");
            preparedStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public static Boolean postPrivilege(String idString, String room) throws Exception {
        try {
            Integer id = Integer.parseInt(idString);
            Connection con = getConnection();

//            Check if UserId exists
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM user WHERE Id = '" + id + "';");
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.next() ) {
                return false;
            }

//            Check for privilege duplication
            preparedStatement = con.prepareStatement("SELECT count(*) AS total FROM privilege WHERE RoomNumber = '" + room + "' AND UserId = '" + id + "';");
            rs = preparedStatement.executeQuery();
            rs.next();
            int result = rs.getInt("total");

            if (result > 0) {
                preparedStatement = con.prepareStatement("DELETE FROM privilege WHERE RoomNumber = '" + room + "' AND UserId = '" + id + "';");
                preparedStatement.executeUpdate();
            }
            preparedStatement = con.prepareStatement("INSERT INTO privilege (RoomNumber, UserId) VALUES ('" + room + "', '" + id + "')");
            preparedStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public static Boolean deleteUser(String email, String password) throws Exception {
        try {
            Connection con = getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("SELECT count(*) AS total FROM user WHERE Email = '" + email + "';");
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            int result = rs.getInt("total");

            if (result == 0) {
                return false;
            } else {
                preparedStatement = con.prepareStatement("DELETE FROM user WHERE Email = '" + email + "' AND Password = '" + password + "';");
                preparedStatement.executeUpdate();
                return true;
            }
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public static Boolean deletePrivilege(String idString, String room) throws Exception {
        try {
            Integer id = Integer.parseInt(idString);
            Connection con = getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("SELECT count(*) AS total FROM privilege WHERE RoomNumber = '" + room + "' AND UserId = '" + id + "';");
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            int result = rs.getInt("total");

            if (result == 0) {
                return false;
            } else {
                preparedStatement = con.prepareStatement("DELETE FROM privilege WHERE RoomNumber = '" + room + "' AND UserId ='" + id + "';");
                preparedStatement.executeUpdate();
                return true;
            }
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public static void createTable() throws Exception {
        try {
            Connection con = getConnection();
            PreparedStatement create = con.prepareStatement("CREATE TABLE IF NOT EXISTS tablename2(id int NOT NULL AUTO_INCREMENT, first varchar(255), last varchar(255), PRIMARY KEY(id))");
            create.executeUpdate();
        } catch(Exception e) {
            System.out.println(e);
        }
        finally {
            System.out.println("Function complete.");
        }
    }

    public static Connection getConnection() throws Exception {
        try {
            String driver = "com.mysql.jdbc.Driver";
            //String url = "jdbc:mysql://localhost:3306/test";
            String url = "jdbc:mysql://localhost:3306/splc";
            String username = "root";
            String password = "";
            Class.forName(driver);

            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected");
            return conn;
        } catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
    public static Boolean checkIfAdminSignIn(String email, String password) throws Exception {
        try {
//            Check for privilege duplication
            Connection con = getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM user WHERE Email = '" + email + "' AND Password = '" + password + "';");
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.next()) {
                return false;
            } else {
                //Boolean isAdmin = rs.getBoolean("IsAdmin");
                if (rs.getBoolean("IsAdmin")) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}
