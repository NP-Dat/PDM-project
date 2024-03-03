package resetPassword;

import action.ConnectDB;
import action.PasswordHash;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
public class RResetPassFrame extends  JFrame {
    private JTextField RusernameField;
    private JPasswordField RnewPasswordField;
    private JButton resetButton;
    private JButton cancelButton;

    public RResetPassFrame() {
        setTitle("Password Reset");
        setLayout(new GridLayout(3, 2));
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        RusernameField = new JTextField(20);
        RnewPasswordField = new JPasswordField(20);
        resetButton = new JButton("Reset Password");
        cancelButton = new JButton("Cancel");

        add(new JLabel("Restaurant Username:"));
        add(RusernameField);
        add(new JLabel("New Password:"));
        add(RnewPasswordField);
        add(resetButton);
        add(cancelButton);

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performPasswordReset();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the frame
            }
        });

        setVisible(true);
    }

    private void performPasswordReset() {
        String username = RusernameField.getText();
        String newPassword = new String(RnewPasswordField.getPassword());

        if(newPassword.isEmpty()){
            JOptionPane.showMessageDialog(this, "Password is empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PasswordHash ph = new PasswordHash();
        String hashedPassword;
        try {
            hashedPassword = ph.passwordHash(newPassword);
        } catch (NoSuchAlgorithmException e) {
            JOptionPane.showMessageDialog(this, "Error hashing password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (resetUserPassword(username, hashedPassword) > 0) {
            JOptionPane.showMessageDialog(this, "Password reset successful!");
        } else {
            JOptionPane.showMessageDialog(this, "Password reset failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int resetUserPassword(String username, String hashedPassword) {
        if (username.isEmpty() || hashedPassword.isEmpty()) {
            return 0;
        }
        try {
            ConnectDB newConn = new ConnectDB();
            String sql = "UPDATE Restaurant SET RestaurantPassword = '" + hashedPassword + "' WHERE RestaurantUsername = '" + username + "'";
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
