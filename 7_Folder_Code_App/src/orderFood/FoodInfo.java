package orderFood;

import action.ConnectDB;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import action.*;

public class FoodInfo extends JFrame {

    private JList<String> restaurantList;
    private List<Integer> restaurantIDs;
    private JTextArea restaurantInfoTextArea;
    private JButton addToCartButton;

    public FoodInfo() {
        this.setTitle("Menu");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        initializeComponents();
        loadRestaurants();
    }

    private void addToCartButtonActionPerformed(ActionEvent evt) {
        int selectedIndex = restaurantList.getSelectedIndex();
        if (selectedIndex != -1) {
            int customerID = SessionData.getInstance().getId();
            AddToCartForm addToCartForm = new AddToCartForm(restaurantIDs.get(selectedIndex));
            addToCartForm.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a restaurant first.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initializeComponents() {
        restaurantList = new JList<>();
        restaurantList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        restaurantList.addListSelectionListener(this::displayRestaurantInfo);

        JScrollPane scrollPane = new JScrollPane(restaurantList);
        restaurantInfoTextArea = new JTextArea();
        restaurantInfoTextArea.setEditable(false);

        addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(this::addToCartButtonActionPerformed);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(restaurantInfoTextArea, GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(addToCartButton))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                        .addComponent(restaurantInfoTextArea)
                                        .addComponent(addToCartButton))
                                .addContainerGap())
        );

        pack();
    }

    private void loadRestaurants() {
        List<String> restaurants = new ArrayList<>();
        restaurantIDs = new ArrayList<>();
        try {
            ConnectDB newConn = new ConnectDB();
            String query = "SELECT RestaurantID, RestaurantName FROM Restaurant";
            try (PreparedStatement statement = newConn.conn.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    restaurantIDs.add(resultSet.getInt(1));
                    restaurants.add(resultSet.getString(2));
                }
            }
            newConn.closeConnection();
        } catch (SQLException e) {
            handleSQLException("Error loading restaurants", e);
        }
        restaurantList.setListData(restaurants.toArray(new String[0]));
    }

    private void displayRestaurantInfo(ListSelectionEvent e) {
        int selectedIndex = restaurantList.getSelectedIndex();
        if (selectedIndex != -1) {
            String restaurantInfo = getRestaurantInfo();
            restaurantInfoTextArea.setText(restaurantInfo);
        }
    }

    private String getRestaurantInfo() {
        int selectedIndex = restaurantList.getSelectedIndex();
        StringBuilder restaurantInfo = new StringBuilder();
        try {
            ConnectDB newConn = new ConnectDB();
            int restaurantId = restaurantIDs.get(selectedIndex);
            String menuQuery = "SELECT FoodName, Price FROM MenuItem WHERE RestaurantID = ?";
            try (PreparedStatement menuStatement = newConn.getConn().prepareStatement(menuQuery)) {
                menuStatement.setInt(1, restaurantId);
                try (ResultSet menuResultSet = menuStatement.executeQuery()) {
                    restaurantInfo.append("Menu for ").append(restaurantList.getSelectedValue()).append(":\n");
                    while (menuResultSet.next()) {
                        String foodName = menuResultSet.getString("FoodName");
                        float price = menuResultSet.getFloat("Price");
                        restaurantInfo.append("- ").append(foodName).append(" ($").append(price).append(")\n");
                    }
                }
            }
        } catch (SQLException e) {
            handleSQLException("Error fetching restaurant information", e);
        }
        return restaurantInfo.toString();
    }

    private void handleSQLException(String message, SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

}
