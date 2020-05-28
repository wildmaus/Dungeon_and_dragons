package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Audio.Audio;
import Objects.*;
import Objects.Enemies.Snail;
import TileMap.*;
import Main.GamePanel;

public class Level1State extends GameState {

    private TileMap tileMap;
    private Background background;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Explosion> explosions;
    private HUD hud;
    private Audio backgroundMusic;

    public Level1State(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    public void init() {

        tileMap = new TileMap(30);
        tileMap.loadTiles("/Tilesets/grasstileset.gif");
        tileMap.loadMap("/Maps/level1-1.map");
        tileMap.setPosition(0, 0);
        background = new Background("/Backgrounds/grassbg1.gif", 0.1);

        player = new Player(tileMap);
        player.setPosition(100, 100);

        createEnemies();

        explosions = new ArrayList<Explosion>();

        hud = new HUD(player);

        backgroundMusic = new Audio("/Music/level1-1.mp3");
        backgroundMusic.play();

    }

    private void createEnemies() {
        enemies = new ArrayList<Enemy>();
        Snail snail;
        Point[] points = new Point[] {
                new Point(200, 200),
                new Point(860, 200),
                new Point(1525, 200),
                new Point(1680, 200),
                new Point(1800, 200),
        };
        for (int i = 0; i < points.length; i ++) {
            snail = new Snail(tileMap);
            snail.setPosition(points[i].x, points[i].y);
            enemies.add(snail);
        }
    }

    public void update() {

        // player
        player.update();
        tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(),GamePanel.HEIGHT / 2 - player.gety());

        // background
        background.setPosition(tileMap.getx(), tileMap.gety());

        // attack enemies
        player.checkAttack(enemies);

        // enemies
        for (int i = 0; i < enemies.size(); i ++) {
            Enemy e = enemies.get(i);
            e.update();
            if (e.isDead()) {
                enemies.remove(i);
                i --;
                explosions.add(new Explosion(e.getx(), e.gety()));
            }
        }

        // explosions
        for (int i = 0; i < explosions.size(); i ++) {
            explosions.get(i).update();
            if (explosions.get(i).shouldRemove()) {
                explosions.remove(i);
                i--;
            }
        }
    }

    public void draw(Graphics2D graphics2D) {

        // deadline
        if (player.isDead()) {
            graphics2D.setColor(Color.RED);
            graphics2D.setFont(new Font("Times New Roman", Font.BOLD, 32));
            graphics2D.drawString("YOU DIED", 90, 90);
            long elapsed = (System.nanoTime() - player.getDeadTimer()) / 1000000;
            backgroundMusic.stop();
            if (elapsed > 1000) {
                gsm.setState(GameStateManager.MENUSTATE);
            }
            return;
        }

        // draw background
        background.draw(graphics2D);

        // draw
        tileMap.draw(graphics2D);

        // player
        player.draw(graphics2D);


        // enemies
        for (int i = 0; i < enemies.size(); i ++) {
            enemies.get(i).draw(graphics2D);
        }

        // explosions
        for (int i = 0; i < explosions.size(); i ++) {
            explosions.get(i).setMapPosition(tileMap.getx(), tileMap.gety());
            explosions.get(i).draw(graphics2D);
        }

        // hud
        hud.draw(graphics2D);

    }
    public void keyPressed(int key) {
        if (key == KeyEvent.VK_LEFT) player.setLeft(true);
        if (key == KeyEvent.VK_RIGHT) player.setRight(true);
        if (key == KeyEvent.VK_UP) player.setUp(true);
        if (key == KeyEvent.VK_DOWN) player.setDown(true);
        if (key == KeyEvent.VK_SPACE) player.setJumping(true);
        if (key == KeyEvent.VK_W) player.setPlanning(true);
        if (key == KeyEvent.VK_Q) player.setScratching();
        if (key == KeyEvent.VK_E) player.setFiring();
    }
    public void keyReleased(int key) {
        if(key == KeyEvent.VK_LEFT) player.setLeft(false);
        if(key == KeyEvent.VK_RIGHT) player.setRight(false);
        if(key == KeyEvent.VK_UP) player.setUp(false);
        if(key == KeyEvent.VK_DOWN) player.setDown(false);
        if(key == KeyEvent.VK_SPACE) player.setJumping(false);
        if(key == KeyEvent.VK_W) player.setPlanning(false);
    }
}
