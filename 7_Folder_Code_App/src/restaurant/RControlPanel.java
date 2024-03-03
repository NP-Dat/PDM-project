package restaurant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RControlPanel extends JPanel {

    public RControlPanel(){
        // Create the "Menu" buttons
        JButton menuButton = new JButton("Menu");
        menuButton.setPreferredSize(new Dimension(200, 100));
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RListMenuFrame rListMenuFrame =new RListMenuFrame();
                rListMenuFrame.setSize(750, 400);
                rListMenuFrame.setVisible(true);

            }
        });

        // Create the "Menu" buttons
        JButton orderButton = new JButton("Order");
        orderButton.setPreferredSize(new Dimension(200, 100));
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RListOrderFrame rListOrderFrame =new RListOrderFrame();
                rListOrderFrame.setSize(750, 400);
                rListOrderFrame.setLocation(700,300);
                rListOrderFrame.setVisible(true);

            }
        });

        // Add all fields in 1 panel
        add(orderButton, BorderLayout.CENTER);
        add(menuButton);
    }
}
