package user;

import action.ConnectDB;
import orderFood.AddToCartForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UCartPanel extends JPanel {
    private double totalCost = 0;

    public UCartPanel(Map<String, Integer> itemQuantity, Map<String, Integer> itemPrice) {
        setLayout(new GridLayout(itemQuantity.keySet().size() + 2, 4));
        add(new JLabel("Item"));
        add(new JLabel("Price"));
        add(new JLabel("Quantity"));
        JButton removeAllButton = new JButton("Remove All");
        removeAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                itemQuantity.clear();
            }
        });
        add(removeAllButton);

        for (String name: itemQuantity.keySet()) {
            add(new JLabel(name));
            add(new JLabel(String.valueOf(itemPrice.get(name))));
            add(new JLabel(String.valueOf(itemQuantity.get(name))));
            JButton removeButton = new JButton("Remove");
            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    itemQuantity.remove(name);
                }
            });
            add(removeButton);

            totalCost += itemPrice.get(name) * itemQuantity.get(name);
        }

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddToCartForm.switchToAddToCartPanel();
            }
        });
        add(backButton);

        JButton checkoutButton = new JButton("Checkout $" + totalCost);
        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddToCartForm.switchToPaymentPanel();
            }
        });
        add(checkoutButton);
    }
}
