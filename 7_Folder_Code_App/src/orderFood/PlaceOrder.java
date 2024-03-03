/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package orderFood;


import action.ConnectDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.sql.*;

/**
 *
 * @author quagghyy
 */
public class PlaceOrder extends JFrame {

    /**
     * Creates new form PlaceOrder
     */
    public PlaceOrder() {

        initializeComponents();
    }

    private void initializeComponents() {
        // Generated code by NetBeans GUI Builder
        placeOrderButton = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        placeOrderButton.setText("Place Order");
        placeOrderButton.addActionListener(this::placeOrderButtonActionPerformed);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(placeOrderButton)
                                .addContainerGap(325, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(placeOrderButton)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }



    private void populateMenuTable() {
        // Fetch menu items from the database and populate the JTable
        DefaultTableModel model = (DefaultTableModel) menuTable.getModel();
        model.setRowCount(0);

        try {
            ConnectDB newConn = new ConnectDB();
            String query = "SELECT FoodName, Price FROM MenuItem";
            try (PreparedStatement statement = newConn.getConn().prepareStatement(query)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String foodName = resultSet.getString("FoodName");
                        int price = resultSet.getInt("Price");
                        model.addRow(new Object[]{foodName, price});
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading menu items", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void placeOrderButtonActionPerformed(ActionEvent evt) {
        // Open the PlaceOrderForm to input delivery details and items
        SwingUtilities.invokeLater(() -> {
            PlaceOrderForm placeOrderForm = new PlaceOrderForm();
            placeOrderForm.setVisible(true);
        });
    }

    


    private JButton placeOrderButton;
    private JScrollPane jScrollPane1;
    private JTable menuTable;}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
