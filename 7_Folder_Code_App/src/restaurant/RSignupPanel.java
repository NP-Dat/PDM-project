package restaurant;

import action.ConnectDB;
import action.PasswordHash;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.Statement;

public class RSignupPanel extends  JPanel{
    private JTextField RusernameField;
    private JTextField RnameField;
    private JTextField RaddressField;
    private JTextField RnumberField;
    private JPasswordField RpasswordField;
    private JButton signupButton;
    private JButton cancelButton;

    public RSignupPanel() {
        setLayout(new GridLayout(6, 2));

        RnameField = new JTextField(20);
        RusernameField = new JTextField(20);
        RaddressField = new JTextField(20);
        RnumberField = new JTextField(20);
        RpasswordField = new JPasswordField(20);
        signupButton = new JButton("Sign Up");
        cancelButton = new JButton("Cancel");

        add(new JLabel("Restaurant Name:"));
        add(RnameField);
        add(new JLabel("Username:"));
        add(RusernameField);
        add(new JLabel("Address:"));
        add(RaddressField);
        add(new JLabel("Phone Number:"));
        add(RnumberField);
        add(new JLabel("Password:"));
        add(RpasswordField);
        add(signupButton);
        add(cancelButton);

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performRSignup();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RHomepageFrame.switchToRLoginPanel();
            }
        });
    }

    private void performRSignup() {
        String Rusername=RusernameField.getText();
        String Rname=RnameField.getText();
        String Rpassword=new String(RpasswordField.getPassword());
        String Raddress=RaddressField.getText();
        String Rnumber=RnumberField.getText();

        if(Rpassword.isEmpty()){
            JOptionPane.showMessageDialog(this, "Password is empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PasswordHash ph = new PasswordHash();//tao object PasswordHash
        String Rpasswordhashed;//tao bien de luu pass sau khi hashed
        try {
            Rpasswordhashed = ph.passwordHash(Rpassword);// hash password dung method cua object
        } catch ( NoSuchAlgorithmException e) {// Exception cua thuat toan ma hoa
            throw new RuntimeException(e);// tra ve exception neu thuat toan ma hoa co loi
        }


        if (registerRUser(Rname, Rusername, Raddress, Rnumber, Rpasswordhashed)>0) {// thay password trong parameter thanh Rpasswordhashed
            JOptionPane.showMessageDialog(this, "Signup successful!");
        } else {
            JOptionPane.showMessageDialog(this, "Signup failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private int registerRUser(String Rname, String Rusername, String Raddress, String Rnumber, String Rpassword) {

        if (Rname.isEmpty() || Rusername.isEmpty() || Raddress.isEmpty() || Rnumber.isEmpty() || Rpassword.isEmpty()) {
            return 0;}
        try  {
            ConnectDB newConn = new ConnectDB();
            String sql = "INSERT INTO Restaurant (RestaurantUserName, RestaurantName, RestaurantAddress, RestaurantNumber, RestaurantPassword) VALUES ('" +Rusername+ "','"+Rname+ "','" +Raddress+ "','" +Rnumber+ "','" +Rpassword+ "')";
            // For demonstration only. Use hashed passwords in production.
            Statement stmt = newConn.getConn().createStatement();
            int rowsRAffected = stmt.executeUpdate(sql);
            return rowsRAffected;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
