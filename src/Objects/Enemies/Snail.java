package Objects.Enemies;

import Objects.Animation;
import Objects.Enemy;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Snail extends Enemy {

    private BufferedImage[] sprites;

    public Snail(TileMap tileMap){
        super(tileMap);

        moveSpeed = 0.3;
        maxSpeed = 0.3;
        width = 30;
        height = 30;
        cwidth = 20;
        cheight = 20;
        fallSpeed = 0.3;
        maxFallSpeed = 5.0;

        health = maxHealth = 2;
        contactDamage = 1;

        // load sprites
        try {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/snail.gif"));
            sprites = new BufferedImage[3];
            for (int i = 0; i < sprites.length; i ++) {
                sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(300);

        right = true;
        facingRight = true;
    }

    private void getNextPosition(){
        // movement
        if (left) {
            dx -= moveSpeed;
            if (dx < - maxSpeed) {
                dx = - maxSpeed;
            }
        }else if(right) {
            dx += moveSpeed;
            if (dx > maxSpeed) {
                dx = maxSpeed;
            }
        }
        // falling
        if (falling) {
            dy += fallSpeed;
            if (dy > 0) jumping = false;
        }

    }

    public void update() {

        //position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemporary, ytemporary);

        // flinching
        if (flinching) {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if (elapsed > 400) {
                flinching = false;
            }
        }

        //turning
        if (right && dx == 0) {
            right = false;
            left = true;
            facingRight = false;
        }
        else if (left && dx == 0) {
            right = true;
            left = false;
            facingRight = true;
        }

        // animation
        animation.update();

    }

    public void draw(Graphics2D graphics2D) {
        //if (notOnScrean()) return;
        setMapPosition();
        super.draw(graphics2D);
    }
}
