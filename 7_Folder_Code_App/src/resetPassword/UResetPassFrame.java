package resetPassword;

import action.ConnectDB;
import action.PasswordHash;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class UResetPassFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField newPasswordField;
    private JButton resetButton;
    private JButton cancelButton;

    public UResetPassFrame() {
        setTitle("Password Reset");
        setLayout(new GridLayout(3, 2));
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        usernameField = new JTextField(20);
        newPasswordField = new JPasswordField(20);
        resetButton = new JButton("Reset Password");
        cancelButton = new JButton("Cancel");

        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("New Password:"));
        add(newPasswordField);
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
        String username = usernameField.getText();
        String newPassword = new String(newPasswordField.getPassword());

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
            String sql = "UPDATE Users SET UserPassword = '" + hashedPassword + "' WHERE UserName = '" + username + "'";
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
