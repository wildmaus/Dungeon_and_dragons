package GameState;

import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuState extends GameState {

    private Background background;

    private String[] options = {
            "Start", "Help", "Quit"
    };
    private int currentChoice = 0;
    private Color titleColor;
    private Font titleFont;
    private Font font;

    public MenuState(GameStateManager gsm) {

        this.gsm = gsm;

        try {
            background = new Background("/Backgrounds/menubg.gif", 1);
            background.setVector(-0.1, 0);

            titleColor = new Color(128, 0,0);
            titleFont = new Font("Century Gothic", Font.PLAIN, 28);
            font = new Font("Cabin", Font.PLAIN, 12);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void init() {}

    public void update() {
        background.update();
    }

    public void draw(Graphics2D graphics2D) {

        // draw bg
        background.draw(graphics2D);

        // draw title
        graphics2D.setColor(titleColor);
        graphics2D.setFont(titleFont);
        graphics2D.drawString("Dangeons and Dragons", 13, 70);

        // draw menu options
        graphics2D.setFont(font);
        for (int i = 0; i < options.length; i++) {
            if (i == currentChoice) {
                graphics2D.setColor(Color.BLACK);
            }
            else {
                graphics2D.setColor(Color.RED);
            }
            graphics2D.drawString(options[i], 145, 140 + i * 15);
        }

    }

    private void select() {

        if (currentChoice == 0) {
            gsm.setState(GameStateManager.LEVEL1STATE);
        }
        if (currentChoice == 1) {
            // help
        }
        if (currentChoice == 2) {
            System.exit(0);
        }
    }
    public void keyPressed(int key) {

        if (key == KeyEvent.VK_ENTER) {
            select();
        }
        if (key == KeyEvent.VK_UP) {
            currentChoice --;
            if(currentChoice == -1) {
                currentChoice = options.length - 1;
            }
        }
        if (key == KeyEvent.VK_DOWN) {
            currentChoice ++;
            if(currentChoice == options.length) {
                currentChoice = 0;
            }
        }
    }

    public void keyReleased(int key) {}
}
