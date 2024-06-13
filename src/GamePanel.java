import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

 public class GamePanel extends JPanel implements ActionListener {
    // Constants and variables
    private int FIELD_WIDTH = 800;
    private int FIELD_HEIGHT = 600;
    private final int BALL_SIZE = 20;
    private final int PLAYER_SIZE = 30;
    private final int OBSTACLE_SIZE = 20;
    private final int GOAL_WIDTH = 100;
    private final int GOAL_HEIGHT = 200;
    private final int MOVE_AMOUNT = 10;
    private final int SHOOT_DISTANCE = 100;
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
    private boolean shoot1Pressed = false;
    private boolean shoot2Pressed = false;
    private boolean player1pos = false;
    private boolean player2pos = false;

    private Timer timer;
    private Obstacle[] player1Obstacles;
    private Obstacle[] player2Obstacles;
    private Random random;
    private Clip goalScoredSound;

    private JFrame mainFrame;
    private MainMenu mainMenu;

     public GamePanel(JFrame mainFrame) {
         this.mainFrame = mainFrame;

         try {
             File soundFile = new File("sound.wav"); // Replace with your sound file path
             AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
             goalScoredSound = AudioSystem.getClip();
             goalScoredSound.open(audioInputStream);
         } catch (Exception e) {
             e.printStackTrace();
         }

         // Get screen dimensions for full screen mode
         Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
         FIELD_WIDTH = (int) screenSize.getWidth();
         FIELD_HEIGHT = (int) screenSize.getHeight();

         setBackground(Color.GREEN);
         setPreferredSize(screenSize); // Set preferred size to screen size
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

         // Initialize other variables
         ballX = FIELD_WIDTH / 2 - BALL_SIZE / 2;
         ballY = FIELD_HEIGHT / 2 - BALL_SIZE / 2;
         player1X = 100;
         player1Y = FIELD_HEIGHT / 2 - PLAYER_SIZE / 2;
         player2X = FIELD_WIDTH - 100 - PLAYER_SIZE;
         player2Y = FIELD_HEIGHT / 2 - PLAYER_SIZE / 2;

         player1Obstacles = new Obstacle[4];
         player2Obstacles = new Obstacle[4];
         random = new Random();

         for (int i = 0; i < 4; i++) {
             player1Obstacles[i] = new Obstacle(random.nextInt(FIELD_WIDTH / 2), random.nextInt(FIELD_HEIGHT), OBSTACLE_SIZE, OBSTACLE_SIZE, MOVE_AMOUNT);
             player2Obstacles[i] = new Obstacle(FIELD_WIDTH / 2 + random.nextInt(FIELD_WIDTH / 2), random.nextInt(FIELD_HEIGHT), OBSTACLE_SIZE, OBSTACLE_SIZE, MOVE_AMOUNT);
         }
     }
     public void actionPerformed(ActionEvent e) {
         moveElements();
         shootBall();
         repaint();
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
            case KeyEvent.VK_SPACE -> {
                if (pressed && player1pos) {
                    shoot1Pressed = true;

                }
            }
            case KeyEvent.VK_ENTER -> {
                if (pressed && player2pos) {
                    shoot2Pressed = true;

                }
            }
        }
    }

    private boolean isPlayer1Turn() {
        // Implement logic based on game state, possession, or time
        // For simplicity, alternate turns based on score difference
        return scorePlayer1 == scorePlayer2 || scorePlayer1 < scorePlayer2; // Player 1's turn if scores are tied or player 1 is behind
    }

    private void shootBall() {
        if (shoot1Pressed) {
            ballX += SHOOT_DISTANCE;
            checkGoalScored(2); // Check if player 1 scored
            shoot1Pressed = false; // Reset the shoot flag
        } else if (shoot2Pressed) {
            ballX -= SHOOT_DISTANCE;
            checkGoalScored(1); // Check if player 2 scored
            shoot2Pressed = false; // Reset the shoot flag
        }
    }
     private void playGoalScoredSound() {
         try {
             if (goalScoredSound != null) {
                 goalScoredSound.stop(); // Stop previous instance if it's still playing
                 goalScoredSound.setFramePosition(0); // Rewind to the beginning
                 goalScoredSound.start(); // Play the sound clip
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
     }

     private void checkGoalScored(int player) {
         if (ballX < 0 && player == 2) {
             scorePlayer2++;
             playGoalScoredSound();
             resetPositions();
         } else if (ballX > FIELD_WIDTH - BALL_SIZE && player == 1) {
             scorePlayer1++;
             playGoalScoredSound();
             resetPositions();
         }
     }
     @Override
     protected void paintComponent(Graphics g) {
         super.paintComponent(g);
         drawField(g);
         drawBall(g);
         drawPlayers(g);
         drawObstacles(g);
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

     private void drawObstacles(Graphics g) {
         g.setColor(Color.BLUE);
         for (Obstacle obstacle : player1Obstacles) {
             g.fillRect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
         }
         g.setColor(Color.RED);
         for (Obstacle obstacle : player2Obstacles) {
             g.fillRect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
         }
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

     private void moveElements() {
         // Move player 1
         if (wPressed) player1Y -= MOVE_AMOUNT;
         if (sPressed) player1Y += MOVE_AMOUNT;
         if (aPressed) player1X -= MOVE_AMOUNT;
         if (dPressed) player1X += MOVE_AMOUNT;

         // Move player 2
         if (upPressed) player2Y -= MOVE_AMOUNT;
         if (downPressed) player2Y += MOVE_AMOUNT;
         if (leftPressed) player2X -= MOVE_AMOUNT;
         if (rightPressed) player2X += MOVE_AMOUNT;

         // Ensure players stay within boundaries
         player1X = Math.max(0, Math.min(player1X, FIELD_WIDTH - PLAYER_SIZE));
         player1Y = Math.max(0, Math.min(player1Y, FIELD_HEIGHT - PLAYER_SIZE));
         player2X = Math.max(0, Math.min(player2X, FIELD_WIDTH - PLAYER_SIZE));
         player2Y = Math.max(0, Math.min(player2Y, FIELD_HEIGHT - PLAYER_SIZE));

         // Check for collisions with obstacles
         for (Obstacle obstacle : player2Obstacles) {
             if (player1X + PLAYER_SIZE > obstacle.x && player1X < obstacle.x + OBSTACLE_SIZE && player1Y + PLAYER_SIZE > obstacle.y && player1Y < obstacle.y + OBSTACLE_SIZE) {
                 ballX = player2X - BALL_SIZE;
                 ballY = player2Y + PLAYER_SIZE / 2 - BALL_SIZE / 2;
             }
         }

         for (Obstacle obstacle : player1Obstacles) {
             if (player2X + PLAYER_SIZE > obstacle.x && player2X < obstacle.x + OBSTACLE_SIZE && player2Y + PLAYER_SIZE > obstacle.y && player2Y < obstacle.y + OBSTACLE_SIZE) {
                 ballX = player1X + PLAYER_SIZE;
                 ballY = player1Y + PLAYER_SIZE / 2 - BALL_SIZE / 2;
             }
         }


         // Move obstacles
         for (Obstacle obstacle : player1Obstacles) {
             obstacle.move();
             if (obstacle.x < 0 || obstacle.x > FIELD_WIDTH / 2 - OBSTACLE_SIZE) {
                 obstacle.reverseDirection();
             }
         }

         for (Obstacle obstacle : player2Obstacles) {
             obstacle.move();
             if (obstacle.x < FIELD_WIDTH / 2 || obstacle.x > FIELD_WIDTH - OBSTACLE_SIZE) {
                 obstacle.reverseDirection();
             }
         }

         // Move the ball with player 1 if it's near player 1
         if (Math.abs(ballX - player1X) < PLAYER_SIZE && Math.abs(ballY - player1Y) < PLAYER_SIZE) {
             ballX = player1X + PLAYER_SIZE;
             ballY = player1Y + PLAYER_SIZE / 2 - BALL_SIZE / 2;
             player1pos = true;



             // Check for collisions with player 1 obstacles
             for (Obstacle obstacle : player1Obstacles) {
                 if (ballX + BALL_SIZE > obstacle.x && ballX < obstacle.x + OBSTACLE_SIZE && ballY + BALL_SIZE > obstacle.y && ballY < obstacle.y + OBSTACLE_SIZE) {
                     ballX = obstacle.x - OBSTACLE_SIZE;
                     ballY = obstacle.y + OBSTACLE_SIZE / 2 - BALL_SIZE / 2;

                 }
             }
         }else{
             player1pos = false;
         }

         // Move the ball with player 2 if it's near player 2
         if (Math.abs(ballX - player2X) < PLAYER_SIZE && Math.abs(ballY - player2Y) < PLAYER_SIZE) {
             ballX = player2X - BALL_SIZE;
             ballY = player2Y + PLAYER_SIZE / 2 - BALL_SIZE / 2;
             player2pos = true;

             // Check for collisions with player 2 obstacles
             for (Obstacle obstacle : player2Obstacles) {
                 if (ballX + BALL_SIZE > obstacle.x && ballX < obstacle.x + OBSTACLE_SIZE && ballY + BALL_SIZE > obstacle.y && ballY < obstacle.y + OBSTACLE_SIZE) {
                     ballX = obstacle.x + OBSTACLE_SIZE;
                     ballY = obstacle.y + OBSTACLE_SIZE / 2 - BALL_SIZE / 2;
                 }
             }
         } else{
             player2pos = false;
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
         player2pos = false;
         player1pos = false;
         ballX = FIELD_WIDTH / 2 - BALL_SIZE / 2;
         ballY = FIELD_HEIGHT / 2 - BALL_SIZE / 2;
         player1X = 100;
         player1Y = FIELD_HEIGHT / 2 - PLAYER_SIZE / 2;
         player2X = FIELD_WIDTH - 100 - PLAYER_SIZE;
         player2Y = FIELD_HEIGHT / 2 - PLAYER_SIZE / 2;
     }
 }