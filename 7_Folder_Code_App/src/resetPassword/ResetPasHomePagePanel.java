package resetPassword;




import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResetPasHomePagePanel {
    private ResetPasHomePageFrame mainFrame;
    public JPanel all = new JPanel();

    // Create Homepage panel that show User or Restaurant
    public ResetPasHomePagePanel(ResetPasHomePageFrame mainFrame){
        this.mainFrame = mainFrame;


        // Create the "User" buttons
        JButton userButton = new JButton("Reset Password For User");
        userButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                UResetPassFrame UR = new UResetPassFrame();
                mainFrame.setVisible(false);
            }
        });

        // Create the "Restaurant" buttons
        JButton restaurantButton = new JButton("Reset Password For Restaurant");
        restaurantButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                RResetPassFrame RHomepageFrame =new RResetPassFrame();
                mainFrame.setVisible(false);
            }
        });


        // Add all fields in 1 panel
        all.add(userButton, BorderLayout.CENTER);
        all.add(restaurantButton);


    }
}
