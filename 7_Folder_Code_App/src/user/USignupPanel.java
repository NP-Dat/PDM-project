package user;

import action.ConnectDB;
import action.PasswordHash;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class USignupPanel extends JPanel{
    private JTextField fullnameField;
    private JTextField usernameField;
    private JTextField addressField;
    private JTextField numberField;
    private JPasswordField passwordField;
    private JButton signupButton;
    private JButton cancelButton;

    public USignupPanel() {
        setLayout(new GridLayout(6, 2));

        fullnameField = new JTextField(20);
        usernameField = new JTextField(20);
        addressField = new JTextField(20);
        numberField = new JTextField(20);
        passwordField = new JPasswordField(20);
        signupButton = new JButton("Sign Up");
        cancelButton = new JButton("Cancel");

        add(new JLabel("Full Name:"));
        add(fullnameField);
        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Address:"));
        add(addressField);
        add(new JLabel("Phone Number:"));
        add(numberField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(signupButton);
        add(cancelButton);

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSignup();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UHomepageFrame.switchToLoginPanel();
            }
        });
    }

    private void performSignup() {
        String username = usernameField.getText();
        String address = addressField.getText();
        String number = numberField.getText();
        String fullname = fullnameField.getText();
        String password = new String(passwordField.getPassword());

        if(password.isEmpty()){
            JOptionPane.showMessageDialog(this, "Password is empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PasswordHash ph = new PasswordHash();//tao object PasswordHash
        String passwordhashed;//tao bien de luu pass sau khi hashed
        try {
            passwordhashed = ph.passwordHash(password);// hash password dung method cua object
        } catch ( NoSuchAlgorithmException e) {// Exception cua thuat toan ma hoa
            throw new RuntimeException(e);// tra ve exception neu thuat toan ma hoa co loi
        }


        if (registerUser(fullname, username, address, number, passwordhashed) > 0) {// thay password trong parameter thanh passwordhashed
            JOptionPane.showMessageDialog(this, "Signup successful!");
        } else {
            JOptionPane.showMessageDialog(this, "Signup failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int registerUser(String fullname, String username, String address, String number, String password) {
        if (fullname.isEmpty() || username.isEmpty() || address.isEmpty() || number.isEmpty() || password.isEmpty()) {
            return 0;}
        try {
            ConnectDB newConn = new ConnectDB();
            String sql = "INSERT INTO Users (UserName, Fullname, UserAddress, UserNumber, UserPassword) VALUES ('" +username+ "','"+fullname+ "','" +address+ "','" +number+ "','" +password+ "')";
             // For demonstration only. Use hashed passwords in production.
            newConn.stmt = newConn.getConn().createStatement();
            int rowsAffected = newConn.stmt.executeUpdate(sql);

            newConn.closeConnection();
            return rowsAffected;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


}
