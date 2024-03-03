
import javax.swing.*;

public class MainFrame extends JFrame {
    private HomepagePanel homepagePanel;


    public MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        this.homepagePanel = new HomepagePanel(this);

        add(homepagePanel.all);
        setLocation(500,0);
        setVisible(true);
    }
}
