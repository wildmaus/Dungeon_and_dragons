package Objects;

import Handler.Creator;
import Handler.Images;
import TileMap.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Fireball extends MapObject {

    private boolean hit;
    private boolean remove;
    private BufferedImage[] sprites;
    private BufferedImage[] hitSprites;

    public Fireball (TileMap tileMap, boolean right) {

        super(tileMap);
        moveSpeed = Creator.shellMoveSpeed;
        if (right) dx = moveSpeed;
        else dx = -moveSpeed;

        width = height = 30;
        cwidth = Creator.shellCwidth;
        cheight = Creator.shellcheight;

        // load sprites
        sprites = Images.fireballs[0];
        hitSprites = Images.fireballs[1];
        animation.setFrames(sprites);
        animation.setDelay(Creator.shellDelay);

    }

    public void setHit() {
        if (hit) return;
        hit = true;
        animation.setFrames(hitSprites);
        animation.setDelay(Creator.shellDelay);
        dx = 0;
    }

    public boolean shouldRemove() {return remove;}

    public void update() {
        checkTileMapCollision();
        setPosition(xtemporary, ytemporary);
        if (dx == 0 && !hit) {
            setHit();
        }
        animation.update();
        if (hit && animation.hasPlayedOnce()){
            remove = true;
        }
    }

    public void draw(Graphics2D graphics2D) {
        setMapPosition();
        super.draw(graphics2D);
    }
}
