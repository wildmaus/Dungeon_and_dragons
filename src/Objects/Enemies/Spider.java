package Objects.Enemies;

import Handler.Creator;
import Handler.Images;
import Objects.Enemy;
import TileMap.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Spider extends Enemy {

    private BufferedImage[] image;
    private int ystart;
    private int yend;
    private long hoverTimer;

    public Spider(TileMap tileMap, int ystart, int asplus) {

        super(tileMap);

        this.ystart = ystart;
        yend = ystart + asplus;
        moveSpeed = Creator.eyMoveSpeed;
        maxSpeed = Creator.eyMaxSpeed;
        stopSpeed = 0.3;
        width = height = 30;
        cwidth = Creator.eyCwidth;
        cheight = Creator.eyCheight;

        health = maxHealth = Creator.eyMaxHealth;
        contactDamage = Creator.eyContactDamage;

        // load sprites
        image = Images.spiider[0];
        animation.setFrames(image);
        animation.setDelay(Creator.eyDelay);

        down = true;
        up = false;
        hoverTimer = System.nanoTime();

    }

    private void getNextPosition() {

        // movement
        if (up) {
            dy -= moveSpeed;
            if (dy < - maxSpeed) {
                dy = - maxSpeed;
            }
            if (y + dy < ystart) {
                dy = 0;
            }
        }
        else if(down) {
            dy += moveSpeed;
            if (dy > maxSpeed) {
                dy = maxSpeed;
            }
            if (y + dy > yend) {
                dy = 0;
            }
        }

    }

    public void update() {
        // position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemporary, ytemporary);

        // turning
        if (down && dy == 0) {
            long elapsed = (System.nanoTime() - hoverTimer) / 1000000;
            if (elapsed > 1000) {
                down = false;
                up = true;
                hoverTimer = System.nanoTime();
            }
        }
        else if (up && dy == 0) {
            long elapsed = (System.nanoTime() - hoverTimer) / 1000000;
            if (elapsed > 1000) {
                down = true;
                up = false;
                hoverTimer = System.nanoTime();
            }
        }

        // flinching
        super.update();

        // animation
        animation.update();
    }

    public void draw(Graphics2D graphics2D) {
        setMapPosition();
        graphics2D.drawLine((int)(x + xmap - 1), (int)(ystart + ymap), (int)(x + xmap - 1), (int)(y + ymap));
        graphics2D.drawLine((int)(x + xmap), (int)(ystart + ymap), (int)(x + xmap), (int)(y + ymap));
        super.draw(graphics2D);
    }
}
