import javax.swing.*;
import java.awt.*;

public class SoccerSimulation extends JFrame {
    private GamePanel gamePanel;

    public SoccerSimulation() {
        setTitle("2D Soccer Simulation");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        gamePanel = new GamePanel();
        add(gamePanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SoccerSimulation::new);
    }
}

