package orderFood;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import action.*;

public class PlaceOrderForm extends JFrame {

    private JTextField deliveryAddressField;
    private JTextField itemQuantityField;
    private JButton addToCartButton;
    private Map<String, Integer> itemQuantities;

    public PlaceOrderForm() {
        this.itemQuantities = new HashMap<>();
        initializeComponents();

    }

    private void initializeComponents() {
        setTitle("Place Order");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel deliveryAddressLabel = new JLabel("Delivery Address:");
        deliveryAddressField = new JTextField();

        JLabel itemQuantityLabel = new JLabel("Item Quantity (e.g., Pizza 2, Burger 1):");
        itemQuantityField = new JTextField();
        addToCartButton = new JButton("Add to Cart");

        panel.add(deliveryAddressLabel);
        panel.add(deliveryAddressField);
        panel.add(itemQuantityLabel);
        panel.add(itemQuantityField);
        panel.add(addToCartButton);

        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToCartButtonActionPerformed(e);
            }
        });

        setLayout(new GridLayout(1, 1));
        add(panel);
    }

    private void addToCartButtonActionPerformed(ActionEvent evt) {
        String itemQuantityString = itemQuantityField.getText();

        String[] items = itemQuantityString.split(",");
        for (String item : items) {
            String[] parts = item.trim().split("\\s+");
            if (parts.length == 2) {
                String itemName = parts[0];
                int quantity = Integer.parseInt(parts[1]);

                itemQuantities.put(itemName, itemQuantities.getOrDefault(itemName, 0) + quantity);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid item quantity format: " + item,
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Now, update the database with the order information and retrieve the actual CartID
        int cartID = updateDatabase();

        itemQuantityField.setText("");
        // Close the PlaceOrderForm after adding items to the cart
        this.dispose();

        // Use the retrieved cartID for further processing or display
        System.out.println("Actual CartID: " + cartID);
    }

    private int updateDatabase() {
        int cartID = -1;  // Default value
        try {
            ConnectDB newConn = new ConnectDB();
            String insertOrderQuery = "INSERT INTO Orders (DeliveryPersonID, CartID, UserID, RestaurantID, DateAndHour, CurrentStatus, DeliveryTime) VALUES (?, ?, ?, ?, ?, ?, ?)";
            String insertOrderItemQuery = "INSERT INTO SelectedItem (FoodID, Quantity, CartID) VALUES (?, ?, ?)";

            try (PreparedStatement orderStatement = newConn.getConn().prepareStatement(insertOrderQuery, PreparedStatement.RETURN_GENERATED_KEYS);
                 PreparedStatement orderItemStatement = newConn.getConn().prepareStatement(insertOrderItemQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {

                orderStatement.setNull(1, Types.INTEGER);
                orderStatement.setInt(2, -1);
                orderStatement.setInt(3, SessionData.getInstance().getId());
                orderStatement.setInt(4, -1);  // Placeholder for RestaurantID
                orderStatement.setString(5, null);
                orderStatement.setString(6, "Preparing");
                orderStatement.setString(7, null);

                orderStatement.executeUpdate();

                // Retrieve the generated order ID and CartID
                int orderId;
                try (ResultSet generatedKeys = orderStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        orderId = generatedKeys.getInt(1);
                        cartID = generatedKeys.getInt("CartID");
                    } else {
                        throw new SQLException("Failed to get order ID, no ID obtained.");
                    }
                }

                for (Map.Entry<String, Integer> entry : itemQuantities.entrySet()) {
                    String foodName = entry.getKey();
                    int quantity = entry.getValue();
                    int foodId = getFoodId(newConn.getConn(), foodName);

                    orderItemStatement.setInt(1, foodId);
                    orderItemStatement.setInt(2, quantity);
                    orderItemStatement.setInt(3, cartID);

                    orderItemStatement.executeUpdate();
                }

                // Commit the transaction
                newConn.getConn().commit();
                newConn.closeConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating database", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return cartID;
    }

    private int getRestaurantIdForCart(int cartID, Connection connection) throws SQLException {
        String query = "SELECT DISTINCT MenuItem.RestaurantID " +
                "FROM SelectedItem " +
                "JOIN MenuItem ON SelectedItem.FoodID = MenuItem.FoodID " +
                "WHERE SelectedItem.CartID = ? " +
                "ORDER BY MenuItem.RestaurantID";  // Add ORDER BY clause

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, cartID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("RestaurantID");
                } else {
                    throw new SQLException("Restaurant ID not found for CartID: " + cartID);
                }
            }
        }
    }

    private int getFoodId(Connection connection, String foodName) throws SQLException {
        String query = "SELECT FoodID FROM MenuItem WHERE FoodName = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, foodName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("FoodID");
                } else {
                    throw new SQLException("Food not found: " + foodName);
                }
            }
        }
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> {
            new PlaceOrderForm().setVisible(true);
        });
    }
}
