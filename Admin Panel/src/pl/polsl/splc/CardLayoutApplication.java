package pl.polsl.splc;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import static pl.polsl.splc.ManageDB.*;
import static pl.polsl.splc.DataValidator.*;

public class CardLayoutApplication implements ItemListener {
    final String currentLoggedEmail; //currently logged admin's email
    JPanel cards; //a panel that uses CardLayout
    final static String cardUserString = "User";
    final static String cardPrivilegeString = "Privilege";

    JButton buttonUserAdd = new JButton("Add");
    JButton buttonUserDelete= new JButton("Delete");
    JTextField textfieldUserEmail = new JTextField("enter email", 20);
    JTextField textfieldUserPassword = new JTextField("set password", 20);

    JButton buttonPrivilegeAdd = new JButton("Add");
    JButton buttonPrivilegeDelete = new JButton("Delete");
    JTextField textfieldPrivilegeID = new JTextField("enter user's ID", 20);
    JTextField textfieldPrivilegeRoom = new JTextField("enter room number", 20);

    public CardLayoutApplication(String givenEmail) {
        currentLoggedEmail = givenEmail;
        JFrame frame = new JFrame("Admin Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponentToPane(frame.getContentPane());
        frame.pack();
        frame.setSize(300, 175);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

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

        textfieldPrivilegeID.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                textfieldPrivilegeID.setText("");
            }
        });

        textfieldPrivilegeRoom.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                textfieldPrivilegeRoom.setText("");
            }
        });

        buttonUserAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    String newEmail = textfieldUserEmail.getText();
                    String newPassword = textfieldUserPassword.getText();

                    if (newEmail.equals("enter email") || newPassword.equals("set password")) {
                        JOptionPane.showMessageDialog(null, "You must type something in the textfield!");
                    } else if (!isValidEmail(newEmail)) {
                        JOptionPane.showMessageDialog(null, "Invalid email.");
                    } else if (newEmail.equals(currentLoggedEmail)) {
                        JOptionPane.showMessageDialog(null, "You can not add yourself.");
                    } else {
                        postUser(newEmail, newPassword);
                        JOptionPane.showMessageDialog(null,"User added.");
                    }
                    textfieldUserEmail.setText("");
                    textfieldUserPassword.setText("");
                } catch(Exception e) {
                    System.out.println(e);
                }
            }
        });

        buttonPrivilegeAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    String newID = textfieldPrivilegeID.getText();
                    String newRoom = textfieldPrivilegeRoom.getText();
                    if (newID.equals("enter user's ID") || newRoom.equals("enter room number")) {
                        JOptionPane.showMessageDialog(null, "You must type something in the textfield!");
                    } else {
                        Boolean postResult = postPrivilege(newID, newRoom);

                        if (postResult){
                            JOptionPane.showMessageDialog(null,"Privilege added.");
                        } else {
                            String message = String.format("There is no user with ID %s to set permission to room number %s.", newID, newRoom);
                            JOptionPane.showMessageDialog(null, message);
                        }
                    }
                    textfieldPrivilegeID.setText("");
                    textfieldPrivilegeRoom.setText("");
                } catch(Exception e) {
                    System.out.println(e);
                }
            }
        });

        buttonUserDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    String emailToDelete = textfieldUserEmail.getText();
                    String passwordToDelete = textfieldUserPassword.getText();
                    if (emailToDelete.equals("enter email") || passwordToDelete.equals("set password")) {
                        JOptionPane.showMessageDialog(null, "You must type something in the textfield!");
                    } else if (!isValidEmail(emailToDelete)) {
                        JOptionPane.showMessageDialog(null, "Invalid email.");
                    }  else if (emailToDelete.equals(currentLoggedEmail)) {
                        JOptionPane.showMessageDialog(null, "You can not delete yourself.");
                    } else {
                        Boolean deleteResult = deleteUser(emailToDelete, passwordToDelete);
                        if (deleteResult){
                            JOptionPane.showMessageDialog(null,"User deleted.");
                        } else {
                            String message = String.format("There is no user %s.", emailToDelete);
                            JOptionPane.showMessageDialog(null, message);
                        }
                        textfieldUserEmail.setText("");
                        textfieldUserPassword.setText("");
                    }
                } catch(Exception e) {
                    System.out.println(e);
                }
            }
        });

        buttonPrivilegeDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    String idToDelete = textfieldPrivilegeID.getText();
                    String roomNumberToDelete = textfieldPrivilegeRoom.getText();
                    if (idToDelete.equals("enter email") || roomNumberToDelete.equals("set password")) {
                        JOptionPane.showMessageDialog(null, "You must type something in the textfield!");
                    } else {
                        Boolean deleteResult = deletePrivilege(idToDelete, roomNumberToDelete);

                        if (deleteResult){
                            JOptionPane.showMessageDialog(null,"Privilege deleted.");
                        } else {
                            String message = String.format("There is no permission for user with ID %s to room number %s.", idToDelete, roomNumberToDelete);
                            JOptionPane.showMessageDialog(null, message);
                        }
                        textfieldPrivilegeID.setText("");
                        textfieldPrivilegeRoom.setText("");
                    }
                } catch(Exception e) {
                    System.out.println(e);
                }
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
        cardPrivilege.add(textfieldPrivilegeID);
        cardPrivilege.add(textfieldPrivilegeRoom);
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
        if (evt.getItem().equals(cardUserString)) {
            textfieldUserEmail.setText("enter email");
            textfieldUserPassword.setText("set password");
        }
        if (evt.getItem().equals(cardPrivilegeString)){
            textfieldPrivilegeID.setText("enter user's ID");
            textfieldPrivilegeRoom.setText("enter room number");
        }
    }
}