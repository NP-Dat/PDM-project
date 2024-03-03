package user;

import action.ConnectDB;
import action.SessionData;
import orderFood.AddToCartForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class UPaymentPanel extends JPanel {
    private final int userID;
    private final int restaurantID;
    private final Map<String, Integer> itemQuantity;
    private final Map<String, Integer> itemPrice;

    private double totalCost = 0;
    String[] payMethodStrings = {"Credit Card", "PayPal", "Cash"};

    private JComboBox paymentMethodComboBox;
    public UPaymentPanel(int restaurantID, Map<String, Integer> itemQuantity, Map<String, Integer> itemPrice){
        this.userID = SessionData.getInstance().getId();
        this.restaurantID = restaurantID;
        this.itemQuantity = itemQuantity;
        this.itemPrice = itemPrice;
        for (String name: itemQuantity.keySet()) {
            totalCost += itemPrice.get(name) * itemQuantity.get(name);
        }

        setLayout(new GridLayout(5,2));
        add(new JLabel("Payment Method:"));
        paymentMethodComboBox = new JComboBox(payMethodStrings);
        add(paymentMethodComboBox);

        String fullName = "", address = "", number = "";
        System.out.println("User ID:" + userID);
        try{
            ConnectDB conn = new ConnectDB();
            String sql = "SELECT FullName, UserAddress, UserNumber FROM Users WHERE UserID = ?";
            PreparedStatement statement = conn.getConn().prepareStatement(sql);
            statement.setInt(1, userID);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                fullName = rs.getString(1);
                address = rs.getString(2);
                number = rs.getString(3);
            }
            conn.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        add(new JLabel("Full Name:"));
        add(new JLabel(fullName));
        add(new JLabel("Address:"));
        add(new JLabel(address));
        add(new JLabel("Phone Number:"));
        add(new JLabel(number));

        JButton backButton = new JButton("Back to Cart");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddToCartForm.switchToCartPanel();
            }
        });
        add(backButton);

        JButton payButton = new JButton("Pay $" + totalCost);
        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMessageSuccessPayment();
            }
        });
        add(payButton);
    }

    private void showMessageSuccessPayment() {
        JOptionPane.showMessageDialog(this, "Your payment has been successful!", "Payment Success", JOptionPane.PLAIN_MESSAGE);
        updateToDatabase();
        UHomepageFrame.switchToUserOrder();
    }

    private int createNewCart() {
        try {
            ConnectDB newConn = new ConnectDB();
            // Check if there's an existing active cart for the user
            String existingCartQuery = "SELECT CartID FROM Cart WHERE CartID = ? AND TotalCost = 0.0";
            try (PreparedStatement existingCartStatement = newConn.getConn().prepareStatement(existingCartQuery)) {
                existingCartStatement.setInt(1, userID);
                try (ResultSet existingCartResultSet = existingCartStatement.executeQuery()) {
                    if (existingCartResultSet.next()) {
                        return existingCartResultSet.getInt("CartID");
                    }
                }
            }

            // If no existing cart, create a new one
            String insertQuery = "INSERT INTO Cart (TotalCost) VALUES (?)";
            String selectQuery = "SELECT SCOPE_IDENTITY() AS CartID";

            try (PreparedStatement insertStatement = newConn.getConn().prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                insertStatement.setDouble(1, totalCost);
                insertStatement.executeUpdate();

                // Retrieve the CartID of the newly created cart
                try (ResultSet resultSet = insertStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        int cartID = resultSet.getInt(1);
                        return cartID;
                    }
                }
            }
            newConn.closeConnection();
        } catch (SQLException e) {
            handleSQLException("Error creating or retrieving cart in the database", e);
        }
        return -1; // Return -1 if failed to create or retrieve a cart
    }

    private void updateToDatabase() {
        try {
            LocalDateTime myDateObj = LocalDateTime.now();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedDate = myDateObj.format(myFormatObj);
            int cartID = createNewCart();

            ConnectDB conn = new ConnectDB();
            String sql;
            PreparedStatement stmt;
            ResultSet rs;

            createSelectedItems(conn, cartID);

            final int orderID = createNewOrder(conn, cartID, formattedDate);
            createNewPayment(conn, orderID, formattedDate);

            conn.closeConnection();
            clearCart();
            AddToCartForm.switchToAddToCartPanel();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearCart() {
        itemQuantity.clear();
    }

    private void createNewPayment(ConnectDB conn, int orderID, String formattedDate) throws SQLException {
        PreparedStatement stmt;
        String sql;
        sql = "INSERT INTO Payment(OrderID, PaymentMethod, PaymentStatus, PaymentTime, Amount)\n" +
                "VALUES (?, ?, 'Completed', ?, ?)";
        stmt = conn.getConn().prepareStatement(sql);
        stmt.setInt(1, orderID);
        stmt.setString(2, paymentMethodComboBox.getSelectedItem().toString());
        stmt.setString(3, formattedDate);
        stmt.setInt(4, (int) totalCost);
        stmt.executeUpdate();
    }

    private void createSelectedItems(ConnectDB conn, int cartID) throws SQLException {
        PreparedStatement stmt;
        String sql;
        sql = "INSERT INTO SelectedItem (FoodID, Quantity, CartID) VALUES ((SELECT FoodID FROM MenuItem WHERE FoodName = ?), ?, ?)";
        stmt = conn.getConn().prepareStatement(sql);
        for (String name: itemQuantity.keySet()) {
            stmt.setString(1, name);
            stmt.setInt(2, itemQuantity.get(name));
            stmt.setInt(3, cartID);
            stmt.executeUpdate();
        }
    }

    private int createNewOrder(ConnectDB conn, int cartID, String formattedDate) throws SQLException {
        String sql;
        PreparedStatement stmt;
        sql = "INSERT INTO Orders(CartID, UserID, RestaurantID, DateAndHour, CurrentStatus)\n" +
                "VALUES (?, ?, ?, ?, 'Pending')";
        stmt = conn.getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setInt(1, cartID);
        stmt.setInt(2, userID);
        stmt.setInt(3, restaurantID);
        stmt.setString(4, formattedDate);
        int affectedRows = stmt.executeUpdate();
        final int orderID;
        if (affectedRows == 0)
            throw new SQLException("Creating order failed, no rows affected.");
        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) orderID = generatedKeys.getInt(1);
            else throw new SQLException("Creating order failed, no ID obtained.");
        }
        System.out.println("Order ID just created: " + orderID);
        return orderID;
    }

    private void handleSQLException(String message, SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
