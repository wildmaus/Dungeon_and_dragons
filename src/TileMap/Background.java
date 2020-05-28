package TileMap;

import GameState.GameState;
import Main.GamePanel;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Background {

    private BufferedImage image;

    private double x;
    private double y;
    private double dx;
    private double dy;

    private double moveScale;

    public Background(String s, double moveScale) {

        try {
            image = ImageIO.read(getClass().getResourceAsStream(s));
            this.moveScale = moveScale;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPosition(double x, double y) {
        this.x = (x * moveScale) % GamePanel.WIDTH;
        this.y = (y * moveScale) % GamePanel.HEIGHT;
    }

    public void setVector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void update() {
        x += dx;
        y += dy;
    }

    public void draw(Graphics2D graphics2D) {
        graphics2D.drawImage(image, (int) x, (int) y, null);
        if (x < 0) {
            graphics2D.drawImage(image, (int) x + GamePanel.WIDTH, (int) y, null);
        }
        if (x > 0) {
            graphics2D.drawImage(image, (int) x - GamePanel.WIDTH, (int) y, null);
        }
    }
}
