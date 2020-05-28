package Objects;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class HUD {

    private Player player;
    private BufferedImage image;
    private Font font;

    public HUD(Player player) {
        this.player = player;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/HUD/hud.gif"));
            font = new Font("Cobin", Font.BOLD, 14);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D graphics2D) {
        graphics2D.drawImage(image, 0, 10, null);
        graphics2D.setFont(font);
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString( player.getHealth() + "/" + player.getMaxHealth(), 30, 25);
        graphics2D.drawString(player.getFire() / 100 + "/" + player.getMaxFire() / 100, 30, 45);
    }
}
