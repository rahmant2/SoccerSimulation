import java.awt.*;

public class Obstacle {
    int x;
    int y;
    int width;
    int height;
    int speed;
    boolean moveRight;

    public Obstacle(int x, int y, int width, int height, int speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.moveRight = true; // Initial movement direction
    }

    public void move() {
        if (moveRight) {
            x += speed;
        } else {
            x -= speed;
        }
    }

    public void reverseDirection() {
        moveRight = !moveRight;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLUE); // Adjust color as needed
        g.fillRect(x, y, width, height);
    }
}
