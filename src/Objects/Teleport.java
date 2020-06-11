package Objects;

import Handler.Creator;
import Handler.Images;
import TileMap.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Teleport extends MapObject {

    private BufferedImage[] sprites;

    public Teleport(TileMap tileMap) {
        super(tileMap);
        facingRight = true;
        width = height = 40;
        cwidth = 20;
        cheight = 40;

        sprites = Images.teleport[0];
        animation.setFrames(sprites);
        animation.setDelay(Creator.endLevelDelay);
    }

    public void update() {
        animation.update();
    }

    public void draw(Graphics2D graphics2D) {
        setMapPosition();
        super.draw(graphics2D);
    }

}
