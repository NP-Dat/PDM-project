package user;

import action.ConnectDB;
import action.PasswordHash;
import action.SessionData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.NoSuchAlgorithmException;

public class ULoginPanel extends JPanel{
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signupButton;

    public ULoginPanel() {
        setLayout(new GridLayout(3, 2));

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        signupButton = new JButton("Signup");

        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(loginButton);
        add(signupButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    performLogin();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UHomepageFrame.switchToSignupPanel();
            }
        });
    }

    private void performLogin() throws SQLException {
        ConnectDB newConn = new ConnectDB();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        PasswordHash ph = new PasswordHash();//tao object PasswordHash
        String passwordhashed;//tao bien de luu pass sau khi hashed
        try {
            passwordhashed = ph.passwordHash(password);// hash password dung method cua object
        } catch (NoSuchAlgorithmException e) {// Exception cua thuat toan ma hoa
            throw new RuntimeException(e);// tra ve exception neu thuat toan ma hoa co loi
        }


        if (authenticateUser(username, passwordhashed)) { // thay password trong parameter thanh passwordhashed
            // User is authenticated
            // Switch to the appropriate panel based on user type
            JOptionPane.showMessageDialog(this, "Login successful!");

            user.UHomepageFrame.switchToUserOrder();


        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        newConn.closeConnection();
    }

    private boolean authenticateUser(String username, String password) {
        try {
            ConnectDB newConn = new ConnectDB();
            String sql = "SELECT UserID FROM Users WHERE UserName = ? AND UserPassword = ?";
            newConn.preparedStmt = newConn.getConn().prepareStatement(sql);
            newConn.preparedStmt.setString(1, username);
            newConn.preparedStmt.setString(2, password);

            ResultSet rs = newConn.preparedStmt.executeQuery();
            if (rs.next()) {
                int userID = rs.getInt("UserID");
                SessionData.getInstance().setId(userID);
                System.out.println("User ID set: " + userID);

                return true;
            }
            newConn.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
