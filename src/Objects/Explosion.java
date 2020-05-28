package Objects;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Explosion {

    private double x;
    private double y;
    private double xmap;
    private double ymap;
    private int width;
    private int height;
    private Animation animation;
    private BufferedImage[] sprites;
    private boolean remove;

    public Explosion(int x, int y) {
        this.x = x;
        this.y = y;
        width = height = 30;

        try {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/explosion.gif"));
            sprites = new BufferedImage[6];
            for (int i = 0; i < sprites.length; i ++) {
                sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(70);

    }

    public void update() {
        animation.update();
        if (animation.hasPlayedOnce()) {
            remove = true;
        }
    }

    public boolean shouldRemove() {
        return remove;
    }

    public void setMapPosition(double x, double y) {
        xmap = x;
        ymap = y;
    }

    public void draw(Graphics2D graphics2D) {
        graphics2D.drawImage(animation.getImage(), (int)(x + xmap - width / 2), (int)(y + ymap - height / 2), null);
    }

}
