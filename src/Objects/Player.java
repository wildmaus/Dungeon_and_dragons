package Objects;

import Audio.Audio;
import TileMap.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class Player extends MapObject {

    // player qualities
    private int health;
    private int maxHealth;
    private int fire;
    private int maxFire;
    private boolean dead;
    private boolean flinching;
    private long flinchTimer;
    private long deadTimer;

    // fireball
    private boolean firing;
    private int fireCost;
    private int fireDamage;
    private ArrayList<Fireball> fireballs;

    // scratch
    private boolean scratching;
    private int scratchDamage;
    private int scratchRange;

    // planning
    private boolean planning;

    // animations
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {2, 8, 1, 2, 4, 2, 5};

    // animation action
    private static final int IDLE = 0;
    private static final int WALKING = 1;
    private static final int JUMPING = 2;
    private static final int FALLING = 3;
    private static final int PLANNING = 4;
    private static final int FIREBALL = 5;
    private static final int SCRATCHING = 6;

    // SFX
    private HashMap<String, Audio> sfx;

    public Player(TileMap tileMap) {
        super(tileMap);

        width = 30;
        height = 30;
        cwidth = 20;
        cheight = 20;
        moveSpeed = 0.3;
        maxSpeed = 1.6;
        stopSpeed = 0.4;
        fallSpeed = 0.15;
        maxFallSpeed = 4.0;
        jumpStart = -4.8;
        jumpStopSpeed = 0.3;
        facingRight = true;
        health = maxHealth = 5;
        fire = maxFire = 2500;
        fireCost = 200;
        fireDamage = 5;
        fireballs = new ArrayList<Fireball>();
        scratchDamage = 8;
        scratchRange = 40;

        // load sprites
        try {

            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/playersprites.gif"));
            sprites = new ArrayList<BufferedImage[]>();
            for (int i = 0; i < 7; i++) {

                BufferedImage[] bufferedImages = new BufferedImage[numFrames[i]];
                for (int j = 0; j < numFrames[i]; j++) {

                    if (i != 6) {
                        bufferedImages[j] = spritesheet.getSubimage(j * width, i * height, width, height);
                    } else {
                        bufferedImages[j] = spritesheet.getSubimage(j * width * 2, i * height, width * 2, height);
                    }

                }

                sprites.add(bufferedImages);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        animation = new Animation();
        currentAction = IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(400);

        sfx = new HashMap<String, Audio>();
        sfx.put("jump", new Audio("/SFX/jump.mp3"));
        sfx.put("scratch", new Audio("/SFX/scratch.mp3"));

    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getFire() {
        return fire;
    }

    public int getMaxFire() {
        return maxFire;
    }

    public boolean isDead() { return dead; }

    public long getDeadTimer() { return deadTimer; }

    public void setFiring() {
        firing = true;
    }

    public void setScratching() {
        scratching = true;
    }

    public void setPlanning(boolean b) {
        planning = b;
    }

    public void checkAttack(ArrayList <Enemy> enemies) {

        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);

            // scratching
            if (scratching) {
                if (facingRight) {
                    if (e.getx() > x && e.getx() < x + scratchRange && e.gety() > y - height / 2 && e.gety() < y + height / 2) {
                        e.hit(scratchDamage);
                    }
                } else {
                    if (e.getx() < x && e.getx() > x - scratchRange && e.gety() > y - height / 2 && e.gety() < y + height / 2) {
                        e.hit(scratchDamage);
                    }
                }
            }

            // fireballs
            for (int j = 0; j < fireballs.size(); j ++) {
                if (fireballs.get(j).intersects(e)) {
                    e.hit(fireDamage);
                    fireballs.get(i).setHit();
                    break;
                }
            }

            // enemy collision
            if (intersects(e)) {
                hit(e.getContactDamage());
            }

        }

    }

    public void  hit(int damage) {
        if (flinching) return;
        health -= damage;
        if (health <= 0) {
            dead = true;
            deadTimer = System.nanoTime();
        }
        flinching = true;
        flinchTimer = System.nanoTime();
    }

    private void getNextPosition() {

        // movement
        if (left) {
            dx -= moveSpeed;
            if (dx < - maxSpeed) {
                dx = - maxSpeed;
            }
        }else if(right) {
            dx += moveSpeed;
            if (dx > maxSpeed) {
                dx = maxSpeed;
            }
        }
        else {
            if (dx > 0) {
                dx -= stopSpeed;
                if (dx < 0) {
                    dx = 0;
                }
            } else if (dx < 0) {
                dx += stopSpeed;
                if (dx > 0) {
                    dx = 0;
                }
            }
        }

        // jumping
        if (jumping && !falling) {
            dy = jumpStart;
            falling = true;
        }

        // falling
        if (falling) {
            if (dy > 0 && planning) dy += fallSpeed * 0.1;
            else dy += fallSpeed;

            if (dy > 0) jumping = false;
            if (dy < 0 && !jumping) dy += jumpStopSpeed;
            if (dy > maxFallSpeed) dy = maxFallSpeed;
        }

    }

    public void update() {

        // position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemporary, ytemporary);

        // check attack has stopped
        if (currentAction == SCRATCHING) {
            if (animation.hasPlayedOnce()) scratching = false;
        }
        if (currentAction == FIREBALL) {
            if (animation.hasPlayedOnce()) firing = false;
        }

        // fireballs
        fire +=1;
        if (fire > maxFire) fire = maxFire;
        if (firing && currentAction != FIREBALL){
            if (fire > fireCost) {
                fire -= fireCost;
                Fireball fireball = new Fireball(tileMap, facingRight);
                fireball.setPosition(x,y);
                fireballs.add(fireball);
            }
        }
        for (int i = 0; i < fireballs.size(); i ++) {
            fireballs.get(i).update();
            if (fireballs.get(i).shouldRemove()) {
                fireballs.remove(i);
                i --;
            }
        }

        // flinching
        if (flinching) {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if (elapsed > 800) {
                flinching = false;
            }
        }

        // set animation
        if (scratching) {
            if (currentAction != SCRATCHING) {
                currentAction = SCRATCHING;
                sfx.get("scratch").play();
                animation.setFrames(sprites.get(SCRATCHING));
                animation.setDelay(50);
                width = 60;
            }
        } else if (firing) {
            if (currentAction != FIREBALL) {
                currentAction = FIREBALL;
                animation.setFrames(sprites.get(FIREBALL));
                animation.setDelay(100);
                width = 30;
            }
        } else if (dy > 0) {
            if (planning) {
                if (currentAction != PLANNING) {
                    currentAction = PLANNING;
                    animation.setFrames(sprites.get(PLANNING));
                    animation.setDelay(100);
                    width = 30;
                }
            } else if (falling) {
                if (currentAction != FALLING) {
                    currentAction = FALLING;
                    animation.setFrames(sprites.get(FALLING));
                    animation.setDelay(100);
                    width = 30;
                }
            }
        } else if (dy < 0) {
            if (currentAction != JUMPING) {
                currentAction = JUMPING;
                sfx.get("jump").play();
                animation.setFrames(sprites.get(JUMPING));
                animation.setDelay(-1);
                width = 30;
            }
        } else if (left || right) {
            if (currentAction != WALKING) {
                currentAction = WALKING;
                animation.setFrames(sprites.get(WALKING));
                animation.setDelay(40);
                width = 30;
            }
        } else if (currentAction != IDLE) {
            currentAction = IDLE;
            animation.setFrames(sprites.get(IDLE));
            animation.setDelay(400);
            width = 30;
        }
        animation.update();

        // direction
        if (currentAction != SCRATCHING && currentAction != FIREBALL) {
            if (right) facingRight = true;
            if (left) facingRight = false;
        }
    }

    public void draw(Graphics2D graphics2D) {

        setMapPosition();

        // fireballs
        for (int i = 0; i < fireballs.size(); i ++) {
            fireballs.get(i).draw(graphics2D);
        }

        // player
        if (flinching) {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if (elapsed / 100 % 2 == 0) return;
        }
        super.draw(graphics2D);

    }

}