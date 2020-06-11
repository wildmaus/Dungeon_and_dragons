package Objects;

import Audio.Audio;
import TileMap.TileMap;

public class Enemy extends MapObject {

    protected int health;
    protected int maxHealth;
    protected boolean dead;
    protected int contactDamage;
    protected boolean flinching;
    protected long flinchTimer;

    public Enemy(TileMap tileMap) {
        super(tileMap);
    }

    public boolean isDead() {return dead;}
    public int getContactDamage() {return contactDamage;}

    public void hit(int damage) {
        if (dead || flinching) return;
        health -= damage;
        Audio.play("enemyhit");
        if (health <= 0) {dead = true;}
        flinching = true;
        flinchTimer = System.nanoTime();
    }

    public void update() {

        // flinching
        if (flinching) {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if (elapsed > 800) {
                flinching = false;
            }
        }

    }
}
