import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;





class GamePanel extends JPanel {
    private final int FIELD_WIDTH = 800;
    private final int FIELD_HEIGHT = 600;
    private final int BALL_SIZE = 20;
    private final int PLAYER_SIZE = 30;
    private final int GOAL_WIDTH = 100;
    private final int GOAL_HEIGHT = 200;
    private final int MOVE_AMOUNT = 10;
    private int ballX = FIELD_WIDTH / 2 - BALL_SIZE / 2;
    private int ballY = FIELD_HEIGHT / 2 - BALL_SIZE / 2;
    private int player1X = 100;
    private int player1Y = FIELD_HEIGHT / 2 - PLAYER_SIZE / 2;
    private int player2X = FIELD_WIDTH - 100 - PLAYER_SIZE;
    private int player2Y = FIELD_HEIGHT / 2 - PLAYER_SIZE / 2;
    private int scorePlayer1 = 0;
    private int scorePlayer2 = 0;

    public GamePanel() {
        setBackground(Color.GREEN);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawField(g);
        drawBall(g);
        drawPlayers(g);
        drawGoals(g);
        drawScore(g);
    }

    private void drawField(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawLine(FIELD_WIDTH / 2, 0, FIELD_WIDTH / 2, FIELD_HEIGHT); // Midfield line
        g.drawOval(FIELD_WIDTH / 2 - 50, FIELD_HEIGHT / 2 - 50, 100, 100); // Center circle
    }

    private void drawBall(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);
    }

    private void drawPlayers(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(player1X, player1Y, PLAYER_SIZE, PLAYER_SIZE);
        g.setColor(Color.RED);
        g.fillRect(player2X, player2Y, PLAYER_SIZE, PLAYER_SIZE);
    }

    private void drawGoals(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, FIELD_HEIGHT / 2 - GOAL_HEIGHT / 2, 10, GOAL_HEIGHT); // Left goal
        g.fillRect(FIELD_WIDTH - 10, FIELD_HEIGHT / 2 - GOAL_HEIGHT / 2, 10, GOAL_HEIGHT); // Right goal
    }

    private void drawScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Player 1: " + scorePlayer1, 50, 30);
        g.drawString("Player 2: " + scorePlayer2, FIELD_WIDTH - 200, 30);
    }

    public void moveElements(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                player1Y -= MOVE_AMOUNT;
                break;
            case KeyEvent.VK_S:
                player1Y += MOVE_AMOUNT;
                break;
            case KeyEvent.VK_A:
                player1X -= MOVE_AMOUNT;
                break;
            case KeyEvent.VK_D:
                player1X += MOVE_AMOUNT;
                break;
            case KeyEvent.VK_UP:
                player2Y -= MOVE_AMOUNT;
                break;
            case KeyEvent.VK_DOWN:
                player2Y += MOVE_AMOUNT;
                break;
            case KeyEvent.VK_LEFT:
                player2X -= MOVE_AMOUNT;
                break;
            case KeyEvent.VK_RIGHT:
                player2X += MOVE_AMOUNT;
                break;
        }

        // Move ball based on players' positions
        if (Math.abs(ballX - player1X) < PLAYER_SIZE && Math.abs(ballY - player1Y) < PLAYER_SIZE) {
            ballX = player1X + PLAYER_SIZE;
            ballY = player1Y + PLAYER_SIZE / 2;
        }
        if (Math.abs(ballX - player2X) < PLAYER_SIZE && Math.abs(ballY - player2Y) < PLAYER_SIZE) {
            ballX = player2X - BALL_SIZE;
            ballY = player2Y + PLAYER_SIZE / 2;
        }

        // Collision with field boundaries
        if (ballX < 0) {
            scorePlayer2++;
            resetBall();
        } else if (ballX > FIELD_WIDTH - BALL_SIZE) {
            scorePlayer1++;
            resetBall();
        }
        if (ballY < 0) {
            ballY = 0;
        } else if (ballY > FIELD_HEIGHT - BALL_SIZE) {
            ballY = FIELD_HEIGHT - BALL_SIZE;
        }

        repaint();
    }

    private void resetBall() {
        ballX = FIELD_WIDTH / 2 - BALL_SIZE / 2;
        ballY = FIELD_HEIGHT / 2 - BALL_SIZE / 2;
    }
}