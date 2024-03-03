package user;

import action.ConnectDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class USearchFoodPanel {
    public  JPanel all;
    private JTable table;
    private JTextField queryField;
    private JButton runButton;

    public USearchFoodPanel() {
        all = new JPanel();
        all.setLayout(new BorderLayout());

        // Panel for query input and "Run" button
        JPanel queryPanel = new JPanel();
        queryPanel.setLayout(new BorderLayout());

        // Query input field
        queryField = new JTextField();
        queryField.setPreferredSize(new Dimension(200, 100));
        queryPanel.add(queryField, BorderLayout.NORTH);

        // "Run" button
        runButton = new JButton("Search the Food you Like !!!!");
        runButton.setPreferredSize(new Dimension(50, 50));
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showQueryResult(queryField.getText());
            }
        });
        queryPanel.add(runButton, BorderLayout.SOUTH);

        all.add(queryPanel, BorderLayout.NORTH);

        // Result table
        table = new JTable();
        all.add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void showQueryResult(String query) {
        if (queryField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please input food name!", "Message", JOptionPane.WARNING_MESSAGE);
            return;}
        try {
            // Connecting to database
            ConnectDB listFoodConn = new ConnectDB();

            String SQL = "SELECT M.FoodName, R.RestaurantName, M.Price " +
                    "FROM MenuItem M " +
                    "JOIN Restaurant R ON M.RestaurantID = R.RestaurantID " +
                    "WHERE M.FoodName LIKE ?";
            PreparedStatement preparedStatement = listFoodConn.getConn().prepareStatement(SQL);
            preparedStatement.setString(1, "%" + queryField.getText() + "%");
            ResultSet rs = preparedStatement.executeQuery();

            // Get metadata
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Setup columns
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setColumnCount(0);
            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(metaData.getColumnLabel(i));
            }

            // Clear old data
            model.setRowCount(0);

            // Add rows
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }

            // Close resources
            rs.close();
            listFoodConn.stmt.close();
            listFoodConn.closeConnection();
        } catch (SQLException e) {
            // Setup columns
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setColumnCount(1);
            model.setColumnIdentifiers(new Object[]{"Error"});

            // Clear old data
            model.setRowCount(0);

            // Add error message
            model.addRow(new Object[]{e.getMessage()});
        }
    }

}

