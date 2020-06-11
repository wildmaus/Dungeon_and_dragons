package Objects.Enemies;

import Handler.Creator;
import Handler.Images;
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

        moveSpeed = Creator.exMoveSpeed;
        maxSpeed = Creator.exMaxSpeed;
        width = height = 30;
        cwidth = Creator.exCwidth;
        cheight = Creator.exCheight;
        fallSpeed = Creator.exFallSpeed;
        maxFallSpeed = Creator.exMaxFallSpeed;

        health = maxHealth = Creator.exMaxHealth;
        contactDamage = Creator.exContactDamage;

        // load sprites
        sprites = Images.snail[0];

        animation.setFrames(sprites);
        animation.setDelay(Creator.exDelay);

        right = true;

    }

    private void getNextPosition() {
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
        // flinching
        super.update();

        // animation
        animation.update();

    }

    public void draw(Graphics2D graphics2D) {
        setMapPosition();
        super.draw(graphics2D);
    }
}
