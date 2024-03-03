package restaurant;

import javax.swing.*;
import java.awt.*;

public class RHomepageFrame extends JFrame{
        private static JFrame mainFrame;

        public RHomepageFrame() {
            SwingUtilities.invokeLater(() -> {
                mainFrame = new JFrame("Food Delivery Application");
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setSize(800, 600);
                switchToRLoginPanel();
                mainFrame.setVisible(true);
            });
        }

        public static void switchToRLoginPanel() {
            mainFrame.getContentPane().removeAll();
            mainFrame.add(new RLoginPanel(mainFrame));
            mainFrame.revalidate();
            mainFrame.repaint();
        }

        public static void switchToRSignupPanel() {
            mainFrame.getContentPane().removeAll();
            mainFrame.add(new RSignupPanel());
            mainFrame.revalidate();
            mainFrame.repaint();
        }

        public static void switchToRControlPanel() {
            mainFrame.getContentPane().removeAll();
            mainFrame.add(new RControlPanel());
            mainFrame.revalidate();
            mainFrame.repaint();
        }
}