package orderFood;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

import user.UAddToCartPanel;
import user.UCartPanel;
import user.UPaymentPanel;

public class AddToCartForm extends JFrame {
    private final int restaurantID;
    private Map<String, Integer> itemQuantity = new HashMap<>();
    private Map<String, Integer> itemPrice = new HashMap<>();
    private static AddToCartForm instance;

    public AddToCartForm(int restaurantID) {
        this.restaurantID = restaurantID;

        setTitle("Add to Cart");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        instance = this;
        switchToAddToCartPanel();
    }

    private void switchToPanel(JPanel panel) {
        instance.getContentPane().removeAll();
        instance.add(panel);
        instance.revalidate();
        instance.repaint();
    }

    public static void switchToAddToCartPanel() {
        instance.switchToPanel(new UAddToCartPanel(instance.restaurantID, instance.itemQuantity, instance.itemPrice));
    }

    public static void switchToCartPanel() {
        instance.switchToPanel(new UCartPanel(instance.itemQuantity, instance.itemPrice));
    }

    public static void switchToPaymentPanel() {
        instance.switchToPanel(new UPaymentPanel(instance.restaurantID, instance.itemQuantity, instance.itemPrice));
    }
}
