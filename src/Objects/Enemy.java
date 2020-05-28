package Objects;

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
        if (health < 0) {dead = true;}
        flinching = true;
        flinchTimer = System.nanoTime();
    }

    public void update() {}
}
