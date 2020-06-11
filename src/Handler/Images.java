package Handler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class Images {

    public static BufferedImage[][] explosion = load(Creator.explosionAnimation, 30, 30);
    public static BufferedImage[][] snail = load(Creator.exAnimation, 30, 30);
    public static BufferedImage[][] spiider = load(Creator.eyAnimation, 30, 30);
    public static BufferedImage[][] fireballs = load(Creator.shellAnimation, 30, 30);
    public static BufferedImage[][] teleport = load(Creator.endLevelAnimation, 40, 40);

    public static BufferedImage[][] load(String path, int w, int h) {
        BufferedImage[][] sprites;
        try {
            BufferedImage spritesheet = ImageIO.read(Images.class.getResourceAsStream(path));
            int width = spritesheet.getWidth() / w;
            int height = spritesheet.getHeight() / h;
            sprites = new BufferedImage[height][width];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    sprites[i][j] = spritesheet.getSubimage(j * w, i * h, w, h);
                }
            }
            return sprites;
        }
        catch(Exception e) {
            e.printStackTrace();
            System.out.println("Error loading graphics.");
            System.exit(0);
        }
        return null;
    }

}
