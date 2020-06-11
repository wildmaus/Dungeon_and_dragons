package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Audio.Audio;
import Handler.Creator;
import Objects.*;
import Objects.Enemies.Snail;
import Objects.Enemies.Spider;
import TileMap.*;
import Main.GamePanel;

public class Level1State extends GameState {

    private TileMap tileMap;
    private Background background;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Explosion> explosions;
    private HUD hud;
    private Teleport teleport;

    public Level1State(GameStateManager gsm, String TileSet, String Map, String Bacground,
                       String BgMusic, Point[] Positions, Point[] PositionsEx, Point[] PositionsEy, int[] EyAsplus) {
        this.gsm = gsm;
        init(TileSet,Map, Bacground, BgMusic, Positions, PositionsEx, PositionsEy, EyAsplus);
    }

    public void init(String TileSet, String Map, String Bacground, String BgMusic, Point[] Positions,
                     Point[] PositionsEx, Point[] PositionsEy, int[] EyAsplus) {

        tileMap = new TileMap(30);
        tileMap.loadTiles(TileSet);
        tileMap.loadMap(Map);
        tileMap.setPosition(0, 0);
        background = new Background(Bacground, 0.1);

        player = new Player(tileMap);
        player.setPosition(Positions[0].x, Positions[0].y);

        teleport = new Teleport(tileMap);
        teleport.setPosition(Positions[1].x, Positions[1].y);

        createEnemies(PositionsEx, PositionsEy, EyAsplus);

        explosions = new ArrayList<Explosion>();

        hud = new HUD(player);

        Audio.load(BgMusic, "bgmusic");
        Audio.loop("bgmusic", 10, Audio.getFrames("bgmusic") - 10);

    }

    private void createEnemies(Point[] PositionsEx, Point[] PositionsEy, int[] EyAsplus) {
        Audio.load(Creator.sfxEnemyHit, "enemyhit");
        Audio.load(Creator.explosionSound, "explode");
        enemies = new ArrayList<Enemy>();
        Snail snail;
        Spider spider;
        for (int i = 0; i < PositionsEx.length; i ++) {
            snail = new Snail(tileMap);
            snail.setPosition(PositionsEx[i].x, PositionsEx[i].y);
            enemies.add(snail);
        }
        for (int i = 0; i < PositionsEy.length; i ++) {
            spider = new Spider(tileMap, PositionsEy[i].y, EyAsplus[i]);
            spider.setPosition(PositionsEy[i].x, PositionsEy[i].y);
            enemies.add(spider);
        }



    }

    public void update() {

        // teleport
        if (teleport.contains(player)) {
            Audio.stop("bgmusic");
            eventFinish();
            return;
        }

        // player
        if (!player.isDead()) {
            player.update();
            tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
        }

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
                explosions.add(new Explosion(tileMap, e.getx(), e.gety()));
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

        // teleport
        teleport.update();
    }

    public void draw(Graphics2D graphics2D) {

        // deadline
        if (player.isDead()) {
            graphics2D.setColor(Color.RED);
            graphics2D.setFont(new Font("Times New Roman", Font.BOLD, 32));
            graphics2D.drawString("YOU DIED", 90, 90);
            long elapsed = (System.nanoTime() - player.getDeadTimer()) / 1000000;
            if (elapsed > 1000) {
                gsm.setState(0);
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
            explosions.get(i).setMapPosition();
            explosions.get(i).draw(graphics2D);
        }

        // hud
        hud.draw(graphics2D);

        // teleport
        teleport.draw(graphics2D);

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

    private void eventFinish() {
        gsm.setState(gsm.getState() + 1);
    }
}
