package user;

import action.ConnectDB;
import action.SessionData;
import orderFood.AddToCartForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.Map;

public class UAddToCartPanel extends JPanel {
    private final int userID;
    private final int restaurantID;
    private final Map<String, Integer> itemQuantity;
    private final Map<String, Integer> itemPrice;

    private JComboBox<String> foodComboBox;
    private JTextField quantityField;
    private JButton addItemButton;
    private JButton viewCartButton;

    public UAddToCartPanel(int restaurantID, Map<String, Integer> itemQuantity, Map<String, Integer> itemPrice) {
        System.out.println("Selected restaurant ID: " + restaurantID);

        this.userID = SessionData.getInstance().getId();
        this.restaurantID = restaurantID;
        this.itemQuantity = itemQuantity;
        this.itemPrice = itemPrice;

        setLayout(new GridLayout(4, 2));

        JLabel foodLabel = new JLabel("Select Food:");
        foodComboBox = new JComboBox<>();
        populateFoodComboBox();

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityField = new JTextField();

        addItemButton = new JButton("Add to Cart");
        addItemButton.addActionListener(this::addItemButtonActionPerformed);

        viewCartButton = new JButton("View Cart");
        viewCartButton.addActionListener(this::viewCartButtonActionPerformed);

        add(foodLabel);
        add(foodComboBox);
        add(quantityLabel);
        add(quantityField);
        add(addItemButton);
        add(viewCartButton);

        setLayout(new GridLayout(3, 2));

        if (viewCartButton != null) {
            add(viewCartButton);
        }
    }

    private void populateFoodComboBox() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        try {
            ConnectDB newConn = new ConnectDB();
            String query = "SELECT FoodName, Price FROM MenuItem WHERE RestaurantID = ?";
            try (PreparedStatement statement = newConn.getConn().prepareStatement(query)) {
                statement.setInt(1, restaurantID);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String foodName = resultSet.getString("FoodName");
                        model.addElement(foodName);
                        itemPrice.put(foodName, resultSet.getInt("Price"));
                    }
                }
            }
            newConn.closeConnection();
        } catch (SQLException e) {
            handleSQLException("Error loading food items", e);
        }
        foodComboBox.setModel(model);
    }

    private void addItemButtonActionPerformed(ActionEvent evt) {
        String selectedFood = (String) foodComboBox.getSelectedItem();
        String quantityString = quantityField.getText();

        if ((!selectedFood.isEmpty() && !quantityString.isEmpty())&&!(Integer.parseInt(quantityString)<1)) {
            try{
                Integer.parseInt(quantityString);
            } catch (Exception e){
                JOptionPane.showMessageDialog(this, "Please select a valid food item and quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int quantity = Integer.parseInt(quantityString);
            itemQuantity.put(selectedFood, itemQuantity.getOrDefault(selectedFood, 0) + quantity);
            quantityField.setText("");
            JOptionPane.showMessageDialog(this, "Item added to cart: " + selectedFood + " (Quantity: " + quantity + ")");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a valid food item and quantity.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeItemButtonActionPerformed(ActionEvent evt) {
        String quantityString = quantityField.getText();
        String selectedFood = (String) foodComboBox.getSelectedItem();
        if (selectedFood != null) {
            int currentQuantity = itemQuantity.getOrDefault(selectedFood, 0);
            if (currentQuantity > 0) {
                try {
                    Integer.parseInt(quantityString);
                }catch (Exception e){
                    JOptionPane.showMessageDialog(this, "Invalid input for this function", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                itemQuantity.put(selectedFood, currentQuantity - Integer.parseInt(quantityString));
                JOptionPane.showMessageDialog(this, "Item removed from cart: " + selectedFood);
            } else {
                JOptionPane.showMessageDialog(this, "No more of this item in the cart.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a food item first.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewCartButtonActionPerformed(ActionEvent evt) {
        AddToCartForm.switchToCartPanel();
    }

    private void handleSQLException(String message, SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}