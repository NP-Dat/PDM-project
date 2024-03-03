package user;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class UOrder {
    public  JPanel all;
    private JButton searchButton;
    private JButton addCartButton;

    public UOrder(JFrame mainFrame) {
        all = new JPanel();


        // Panel for query input and "Run" button
        JPanel queryPanel = new JPanel();
        queryPanel.setLayout(new BorderLayout());

        // "Run" button
        searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(200, 50));
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame userSearch = new JFrame();
                USearchFoodPanel USearchFoodPanel = new USearchFoodPanel();
                userSearch.add(USearchFoodPanel.all);
                userSearch.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                userSearch.setSize(600, 400);
                userSearch.setLocation(50, 250);
                userSearch.setVisible(true);
            }
        });
        queryPanel.add(searchButton);

        // "Run" button
        addCartButton = new JButton("Add Cart");
        addCartButton.setPreferredSize(new Dimension(200, 50));
        addCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show the FoodInfo GUI
                orderFood.FoodInfo foodinfo = new orderFood.FoodInfo();
                foodinfo.setLocation(900,300);
                foodinfo.setVisible(true);

            }
        });

        all.add(searchButton, BorderLayout.CENTER);
        all.add(addCartButton);

    }



}

