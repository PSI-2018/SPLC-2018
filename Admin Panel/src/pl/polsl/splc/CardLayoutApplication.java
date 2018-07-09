package pl.polsl.splc;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.*;

public class CardLayoutApplication implements ItemListener {
    JPanel cards; //a panel that uses CardLayout
    final static String cardUserString = "User";
    final static String cardPrivilegeString = "Privilege";
    final static String cardUserAddString = "userAdd";
    //JPanel cardsUser;

    JButton buttonUserAdd = new JButton("Add");
    JButton buttonUserDelete= new JButton("Delete");
    JTextField textfieldUserEmail = new JTextField("enter email", 20);
    JTextField textfieldUserPassword = new JTextField("set password", 20);

    JButton buttonPrivilegeAdd = new JButton("Add");
    JButton buttonPrivilegeDelete = new JButton("Delete");
    JTextField textfieldPrivilegeEmail = new JTextField("enter user's ID", 20);
    JTextField textfieldPrivilegePassword = new JTextField("enter room number", 20);

    public CardLayoutApplication() {
        textfieldUserEmail.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                textfieldUserEmail.setText("");
            }
        });

        textfieldUserPassword.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                textfieldUserPassword.setText("");
            }
        });

        textfieldPrivilegeEmail.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                textfieldPrivilegeEmail.setText("");
            }
        });
        textfieldPrivilegePassword.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                textfieldPrivilegePassword.setText("");
            }
        });

        buttonUserAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JOptionPane.showMessageDialog(null,"User added.");
                try {
                    createTable();
                } catch(Exception e) {
                    System.out.println(e);
                }
            }
        });

        buttonUserDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"User deleted.");
            }
        });

        buttonPrivilegeAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"Privilege added.");
            }
        });

        buttonPrivilegeDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"Privilege deleted.");
            }
        });
    }

    public void addComponentToPane(Container pane) {
        //Put the JComboBox in a JPanel to get a nicer look.
        JPanel comboBoxPane = new JPanel(); //use FlowLayout
        String comboBoxItems[] = { cardUserString, cardPrivilegeString };
        JComboBox comboBox = new JComboBox(comboBoxItems);

        //comboBox.setEditable(false);
        comboBox.addItemListener(this);
        comboBoxPane.add(comboBox);

        //Create the "cards".
        JPanel cardUser = new JPanel();
        cardUser.add(textfieldUserEmail);
        cardUser.add(textfieldUserPassword);
        cardUser.add(buttonUserAdd);
        cardUser.add(buttonUserDelete);

        JPanel cardPrivilege = new JPanel();
        cardPrivilege.add(textfieldPrivilegeEmail);
        cardPrivilege.add(textfieldPrivilegePassword);
        cardPrivilege.add(buttonPrivilegeAdd);
        cardPrivilege.add(buttonPrivilegeDelete);

        //Create the panel that contains the "cards".
        cards = new JPanel(new CardLayout());
        cards.add(cardUser, cardUserString);
        cards.add(cardPrivilege, cardPrivilegeString);

        pane.add(comboBoxPane, BorderLayout.PAGE_START);
        pane.add(cards, BorderLayout.CENTER);
    }

    public void itemStateChanged(ItemEvent evt) {
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, (String)evt.getItem());
    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Admin Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        CardLayoutApplication demo = new CardLayoutApplication();
        demo.addComponentToPane(frame.getContentPane());

        //Display the window.
        frame.pack();
        frame.setSize(300, 175);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws Exception{
        /* Use an appropriate Look and Feel */
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);

        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    public static void createTable() throws Exception {
        try{
            Connection con = getConnection();
            PreparedStatement create = con.prepareStatement("CREATE TABLE IF NOT EXISTS tablename2(id int NOT NULL AUTO_INCREMENT, first varchar(255), last varchar(255), PRIMARY KEY(id))");
            create.executeUpdate();
        }catch(Exception e){
            System.out.println(e);
        }
        finally{
            System.out.println("Function complete.");
        }
    }

    public static Connection getConnection() throws Exception {
        try {
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/test";
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
}