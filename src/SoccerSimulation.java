import javax.swing.*;
import java.awt.*;

import javax.swing.*;

public class SoccerSimulation {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame mainFrame = new JFrame("Game Menu");
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.getContentPane().add(new MainMenu(mainFrame));
                mainFrame.pack();
                mainFrame.setLocationRelativeTo(null); // Center the frame
                mainFrame.setVisible(true);
            }
        });
    }
}
