package restaurant;

import action.ConnectDB;
import action.SessionData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RListOrderFrame extends JFrame {
    private JComboBox<String> ordersBox;
    private JComboBox<String> deliveryPersonBox;
    private JButton assignButton;
    private ConnectDB conn;

    public RListOrderFrame() {
        try {
            conn = new ConnectDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JLabel ordersLabel = new JLabel("Select Order Number:");
        ordersBox = new JComboBox<>();
        JLabel deliveryPersonLabel = new JLabel("Select Delivery Person:");
        deliveryPersonBox = new JComboBox<>();
        assignButton = new JButton("Assign");
        assignButton.setPreferredSize(new Dimension(200,100));

        // Set the preferred size of the JComboBoxes
        ordersBox.setPreferredSize(new Dimension(400, 200));
        deliveryPersonBox.setPreferredSize(new Dimension(400, 200));

        add(ordersLabel);
        add(ordersBox);
        add(deliveryPersonLabel);
        add(deliveryPersonBox);
        add(assignButton);

        populateOrders();
        populateDeliveryPersons();

        assignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOrder = (String) ordersBox.getSelectedItem();
                String selectedDeliveryPerson = (String) deliveryPersonBox.getSelectedItem();
                int deliveryPersonID = -1;
                try {
                    ConnectDB conn = new ConnectDB();

                    conn.preparedStmt = conn.getConn().prepareStatement("SELECT DeliveryID FROM DeliveryPerson WHERE DName = ? AND PersonStatus = 'Available'");
                    conn.preparedStmt.setString(1, selectedDeliveryPerson);

                    ResultSet rs = conn.preparedStmt.executeQuery();

                    if (rs.next()) {
                        deliveryPersonID = rs.getInt("DeliveryID");
                        System.out.println("The DeliveryID for " + selectedDeliveryPerson + " is " + deliveryPersonID);
                    } else {
                        System.out.println("No DeliveryPerson found with DName " + selectedDeliveryPerson);
                    }

                    conn.closeConnection();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }

                updateOrder(selectedOrder, deliveryPersonID);
                updateDeli(deliveryPersonID);
                populateOrders();
                populateDeliveryPersons();
            }
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void populateOrders() {
        try {
            ordersBox.removeAllItems();
            ResultSet rs = conn.stmt.executeQuery("SELECT OrderID FROM Orders WHERE CurrentStatus = 'Pending' AND RestaurantID = '" + SessionData.getInstance().getId() + "' ;");
            while (rs.next()) {
                ordersBox.addItem(rs.getString("OrderID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateDeliveryPersons() {
        try {
            deliveryPersonBox.removeAllItems();
            ResultSet rs = conn.stmt.executeQuery("SELECT DName, DeliveryID FROM DeliveryPerson WHERE PersonStatus = 'Available'");
            while (rs.next()) {
                deliveryPersonBox.addItem(rs.getString("DName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateOrder(String orderId, int deliveryPersonID) {
        try {
            LocalDateTime myDateObj = LocalDateTime.now();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedDate = myDateObj.format(myFormatObj);
            conn.preparedStmt = conn.getConn().prepareStatement("UPDATE Orders SET DeliveryPersonID = ? , DeliveryTime = ? , CurrentStatus = 'Preparing' WHERE OrderID = ?");
            conn.preparedStmt.setInt(1, deliveryPersonID);
            conn.preparedStmt.setString(2, formattedDate);
            conn.preparedStmt.setString(3, orderId);
            conn.preparedStmt.executeUpdate();
            if(orderId==null){
                JOptionPane.showMessageDialog(this, "No order to assign");
                return;
            }

            JOptionPane.showMessageDialog(this, "Assign Delivery person successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Cannot Update", "Error", JOptionPane.ERROR_MESSAGE);

        }
    }

    private void updateDeli(int deliveryPersonID) {
        try {

            conn.preparedStmt = conn.getConn().prepareStatement("UPDATE DeliveryPerson SET PersonStatus = 'Shipping' WHERE DeliveryID = ?");
            conn.preparedStmt.setInt(1, deliveryPersonID);
            conn.preparedStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Cannot Update", "Error", JOptionPane.ERROR_MESSAGE);

        }
    }


}
