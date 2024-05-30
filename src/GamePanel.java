import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class GamePanel extends JPanel implements ActionListener {
    private final int FIELD_WIDTH = 800;
    private final int FIELD_HEIGHT = 600;
    private final int BALL_SIZE = 20;
    private final int PLAYER_SIZE = 30;
    private final int GOAL_WIDTH = 100;
    private final int GOAL_HEIGHT = 200;
    private final int MOVE_AMOUNT = 5;
    private int ballX = FIELD_WIDTH / 2 - BALL_SIZE / 2;
    private int ballY = FIELD_HEIGHT / 2 - BALL_SIZE / 2;
    private int player1X = 100;
    private int player1Y = FIELD_HEIGHT / 2 - PLAYER_SIZE / 2;
    private int player2X = FIELD_WIDTH - 100 - PLAYER_SIZE;
    private int player2Y = FIELD_HEIGHT / 2 - PLAYER_SIZE / 2;
    private int scorePlayer1 = 0;
    private int scorePlayer2 = 0;

    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean wPressed = false;
    private boolean sPressed = false;
    private boolean aPressed = false;
    private boolean dPressed = false;

    private Timer timer;

    public GamePanel() {
        setBackground(Color.GREEN);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e.getKeyCode(), true);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                handleKeyPress(e.getKeyCode(), false);
            }
        });
        timer = new Timer(20, this);
        timer.start();
    }

    private void handleKeyPress(int keyCode, boolean pressed) {
        switch (keyCode) {
            case KeyEvent.VK_W -> wPressed = pressed;
            case KeyEvent.VK_S -> sPressed = pressed;
            case KeyEvent.VK_A -> aPressed = pressed;
            case KeyEvent.VK_D -> dPressed = pressed;
            case KeyEvent.VK_UP -> upPressed = pressed;
            case KeyEvent.VK_DOWN -> downPressed = pressed;
            case KeyEvent.VK_LEFT -> leftPressed = pressed;
            case KeyEvent.VK_RIGHT -> rightPressed = pressed;
        }
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

    @Override
    public void actionPerformed(ActionEvent e) {
        moveElements(e);
        repaint();
    }

    private void moveElements(ActionEvent e) {
        if (wPressed) player1Y -= MOVE_AMOUNT;
        if (sPressed) player1Y += MOVE_AMOUNT;
        if (aPressed) player1X -= MOVE_AMOUNT;
        if (dPressed) player1X += MOVE_AMOUNT;
        if (upPressed) player2Y -= MOVE_AMOUNT;
        if (downPressed) player2Y += MOVE_AMOUNT;
        if (leftPressed) player2X -= MOVE_AMOUNT;
        if (rightPressed) player2X += MOVE_AMOUNT;

        // Ensure players stay within boundaries
        player1X = Math.max(0, Math.min(player1X, FIELD_WIDTH - PLAYER_SIZE));
        player1Y = Math.max(0, Math.min(player1Y, FIELD_HEIGHT - PLAYER_SIZE));
        player2X = Math.max(0, Math.min(player2X, FIELD_WIDTH - PLAYER_SIZE));
        player2Y = Math.max(0, Math.min(player2Y, FIELD_HEIGHT - PLAYER_SIZE));

        // Move ball based on players' positions
        if (Math.abs(ballX - player1X) < PLAYER_SIZE && Math.abs(ballY - player1Y) < PLAYER_SIZE) {
            ballX = player1X + PLAYER_SIZE;
            ballY = player1Y + PLAYER_SIZE / 2 - BALL_SIZE / 2;
        }
        if (Math.abs(ballX - player2X) < PLAYER_SIZE && Math.abs(ballY - player2Y) < PLAYER_SIZE) {
            ballX = player2X - BALL_SIZE;
            ballY = player2Y + PLAYER_SIZE / 2 - BALL_SIZE / 2;
        }

        // Collision with field boundaries and scoring
        if (ballX < 0) {
            scorePlayer2++;
            resetPositions();
        } else if (ballX > FIELD_WIDTH - BALL_SIZE) {
            scorePlayer1++;
            resetPositions();
        }
        if (ballY < 0) {
            ballY = 0;
        } else if (ballY > FIELD_HEIGHT - BALL_SIZE) {
            ballY = FIELD_HEIGHT - BALL_SIZE;
        }
    }

    private void resetPositions() {
        ballX = FIELD_WIDTH / 2 - BALL_SIZE / 2;
        ballY = FIELD_HEIGHT / 2 - BALL_SIZE / 2;
        player1X = 100;
        player1Y = FIELD_HEIGHT / 2 - PLAYER_SIZE / 2;
        player2X = FIELD_WIDTH - 100 - PLAYER_SIZE;
        player2Y = FIELD_HEIGHT / 2 - PLAYER_SIZE / 2;
    }
}
