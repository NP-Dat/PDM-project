package user;

import javax.swing.*;

public class UHomepageFrame extends JFrame{
    private static JFrame mainFrame;

    public UHomepageFrame() {
        SwingUtilities.invokeLater(() -> {
            mainFrame = new JFrame("Food Delivery Application");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(600, 350);
            switchToLoginPanel();
            mainFrame.setLocation(500,0);
            mainFrame.setVisible(true);
        });
    }

    public static void switchToLoginPanel() {
        mainFrame.getContentPane().removeAll();
        mainFrame.add(new ULoginPanel());
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public static void switchToSignupPanel() {
        mainFrame.getContentPane().removeAll();
        mainFrame.add(new USignupPanel());
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public static void switchToUserOrder(){
        mainFrame.getContentPane().removeAll();
        UOrder UOrder = new UOrder(mainFrame);
        mainFrame.add(UOrder.all);
        mainFrame.revalidate();
        mainFrame.repaint();
    }
}