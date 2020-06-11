package Objects;

import Audio.Audio;
import Handler.Creator;
import Handler.Images;
import TileMap.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Explosion extends MapObject{
    
    private BufferedImage[] sprites;
    private boolean remove;

    public Explosion(TileMap tileMap, int x, int y) {
        super(tileMap);
        this.x = x;
        this.y = y;
        width = height = 30;

        sprites = Images.explosion[0];


        animation.setFrames(sprites);
        animation.setDelay(Creator.explosinDelay);
        Audio.play("explode");
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

    public void draw(Graphics2D graphics2D) {
        super.draw(graphics2D);
    }

}
