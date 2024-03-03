package resetPassword;

import javax.swing.*;

public class ResetPasHomePageFrame extends JFrame {
    private static JFrame mainFrame;
    private ResetPasHomePagePanel resetPasHomePagePanel;

    public ResetPasHomePageFrame() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        this.resetPasHomePagePanel = new ResetPasHomePagePanel(this);

        add(resetPasHomePagePanel.all);
        setLocation(500,300);
        setVisible(true);
    }


}
