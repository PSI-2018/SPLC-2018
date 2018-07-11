package pl.polsl.splc;

import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;

import static pl.polsl.splc.ManageDB.*;
import static pl.polsl.splc.DataValidator.*;

public class Log extends JFrame {

    public static void main(String[] args) {
        Log logFrame = new Log();
    }

    JButton buttonLogIn= new JButton("Log in");
    JPanel panel = new JPanel();
    JTextField textfieldEmail = new JTextField(20);
    JPasswordField textfieldPassword = new JPasswordField(20);

    Log() {
        super("Login Autentification");
        textfieldEmail.setBounds(70,30,150,20);
        textfieldPassword.setBounds(70,65,150,20);
        buttonLogIn.setBounds(110,100,80,20);

        panel.add(buttonLogIn);
        panel.add(textfieldEmail);
        panel.add(textfieldPassword);
        setSize(300,200);
        setLocationRelativeTo(null);
        panel.setLayout (null);

        getContentPane().add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        actionlogin();
    }

    public void actionlogin(){
        textfieldEmail.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                textfieldEmail.setText("");
            }
        });

        textfieldPassword.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                textfieldPassword.setText("");
            }
        });

        buttonLogIn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    String email = textfieldEmail.getText();
//                    char ppaswd[] = textfieldPassword.getPassword();
                    String password = textfieldPassword.getText();

                    if (!isValidEmail(email)) {
                        JOptionPane.showMessageDialog(null, "Invalid email.");
                    } else {
                        Boolean result = checkIfAdminSignIn(email, password);
                        if(result) {
                            String message = String.format("Hello again %s!", email);
                            JOptionPane.showMessageDialog(null, message);
                            CardLayoutApplication mainApp = new CardLayoutApplication(email);
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(null,"Wrong email / password.");
                        }
                    }
                    textfieldEmail.setText("");
                    textfieldPassword.setText("");
                    textfieldEmail.requestFocus();
                } catch(Exception e) {
                    System.out.println(e);
                }
            }
        });
    }
}