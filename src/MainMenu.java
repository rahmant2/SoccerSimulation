import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {
    private JFrame mainFrame;
    private JButton startButton;
    private JButton exitButton;

    public MainMenu(JFrame mainFrame) {
        this.mainFrame = mainFrame;
        setupUI();
    }

    private void setupUI() {
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK); // Set background color

        // Create title label
        JLabel titleLabel = new JLabel("Game Title");
        titleLabel.setForeground(Color.WHITE); // Set text color
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36)); // Set font
        GridBagConstraints titleConstraints = new GridBagConstraints();
        titleConstraints.gridx = 0;
        titleConstraints.gridy = 0;
        titleConstraints.gridwidth = 2;
        titleConstraints.insets = new Insets(50, 0, 50, 0); // Add padding
        add(titleLabel, titleConstraints);

        // Create start button
        startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.PLAIN, 24)); // Set button font
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchToGamePanel(); // Switch to GamePanel
            }
        });
        GridBagConstraints startButtonConstraints = new GridBagConstraints();
        startButtonConstraints.gridx = 0;
        startButtonConstraints.gridy = 1;
        startButtonConstraints.insets = new Insets(10, 0, 10, 10); // Add padding
        add(startButton, startButtonConstraints);

        // Create exit button
        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 24)); // Set button font
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the application
            }
        });
        GridBagConstraints exitButtonConstraints = new GridBagConstraints();
        exitButtonConstraints.gridx = 1;
        exitButtonConstraints.gridy = 1;
        exitButtonConstraints.insets = new Insets(10, 10, 10, 0); // Add padding
        add(exitButton, exitButtonConstraints);
    }

    private void switchToGamePanel() {
        mainFrame.getContentPane().removeAll();
        GamePanel gamePanel = new GamePanel(mainFrame);
        mainFrame.getContentPane().add(gamePanel);
        mainFrame.pack();
        mainFrame.repaint();
        gamePanel.requestFocusInWindow(); // Set focus to GamePanel
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Custom painting code if needed
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600); // Set preferred size for the menu panel
    }
}
