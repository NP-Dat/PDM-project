

import resetPassword.ResetPasHomePageFrame;
import restaurant.RHomepageFrame;
import user.UHomepageFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomepagePanel {
    private MainFrame mainFrame;
    public JPanel all = new JPanel();

    // Create Homepage panel that show User or Restaurant
    public HomepagePanel(MainFrame mainFrame){
        this.mainFrame = mainFrame;


        // Create the "User" and "Restaurant" buttons
        JButton userButton = new JButton("User");
        userButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open user login/signup window
                UHomepageFrame UHomepageFrame =new UHomepageFrame();
                mainFrame.setVisible(false);
            }
        });

        JButton restaurantButton = new JButton("Restaurant");
        restaurantButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RHomepageFrame RHomepageFrame =new RHomepageFrame();
                mainFrame.setVisible(false);
            }
        });

        JButton resetPasswordButton = new JButton("Reset Password");
        resetPasswordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ResetPasHomePageFrame resetPasHomePageFrame =new ResetPasHomePageFrame();

            }
        });



        // Add all fields in 1 panel
        all.add(userButton, BorderLayout.CENTER);
        all.add(restaurantButton);
        all.add(resetPasswordButton);



    }
}
