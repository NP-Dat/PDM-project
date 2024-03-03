package restaurant;

import action.ConnectDB;
import action.PasswordHash;
import action.SessionData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RLoginPanel extends JPanel{
    private JTextField RusernameField;
    private JPasswordField RpasswordField;
    private JButton RloginButton;
    private JButton RsignupButton;
    private JFrame mainFrame;


    public RLoginPanel(JFrame mainFrame ) {
        this.mainFrame = mainFrame;

        setLayout(new GridLayout(3, 2));

        RusernameField = new JTextField(20);
        RpasswordField = new JPasswordField(20);
        RloginButton = new JButton("Login");
        RsignupButton = new JButton("Signup");

        add(new JLabel("Username:"));
        add(RusernameField);
        add(new JLabel("Password:"));
        add(RpasswordField);
        add(RloginButton);
        add(RsignupButton);

        RloginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    performRLogin();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        RsignupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RHomepageFrame.switchToRSignupPanel();
            }
        });
    }

    private void performRLogin() throws SQLException {
        ConnectDB newConn = new ConnectDB();
        String Rusername = RusernameField.getText();
        String Rpassword = new String(RpasswordField.getPassword());

        PasswordHash ph = new PasswordHash();//tao object cua PasswordHash
        String Rpasswordhashed;//tao bien de luu pass sau khi hashed
        try {
            Rpasswordhashed = ph.passwordHash(Rpassword);// hash Rpassword dung method cua object
        } catch (NoSuchAlgorithmException e) {// Exception cua thuat toan ma hoa
            throw new RuntimeException(e);// tra ve exception neu thuat toan ma hoa co loi
        }

        if (authenticateRUser(Rusername, Rpasswordhashed)) { // thay password trong parameter thanh Rpasswordhashed
            // User is authenticated
            // Switch to the appropriate panel based on user type
            JOptionPane.showMessageDialog(this, "Login successful!");

            RHomepageFrame.switchToRControlPanel();

//            RListMenu rListMenu=new RListMenu();
//            rListMenu.setSize(750, 400);
//            rListMenu.setVisible(true);
//            mainFrame.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        newConn.closeConnection();
    }

    private boolean authenticateRUser(String Rusername, String Rpassword) {
        try {
            ConnectDB newConn = new ConnectDB();
            String sql = "SELECT RestaurantID FROM Restaurant WHERE RestaurantUserName = ? AND RestaurantPassword = ?";
            PreparedStatement stmt = newConn.getConn().prepareStatement(sql);
            stmt.setString(1, Rusername);
            stmt.setString(2, Rpassword); // For demonstration only. Use hashed passwords in production.

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int restaurantID = rs.getInt("RestaurantID");
                SessionData.getInstance().setId(restaurantID);
                System.out.println("Restaurant ID set: " + restaurantID);

                return true;
            }
            newConn.closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
