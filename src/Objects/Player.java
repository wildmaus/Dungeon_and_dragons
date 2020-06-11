package Objects;

import Audio.Audio;
import Handler.Creator;
import TileMap.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
    private int restorationFire;
    private ArrayList<Fireball> fireballs;

    // scratch
    private boolean scratching;
    private int scratchDamage;
    private int scratchRange;

    // planning
    private boolean planning;

    // animations
    private ArrayList<BufferedImage[]> sprites;
    private int[] numFrames;
    private int[] FrameWidths;
    private int[] FrameHeights;
    private int[] SpriteDelays;

    // animation action
    private static final int IDLE = 0;
    private static final int WALKING = 1;
    private static final int JUMPING = 2;
    private static final int FALLING = 3;
    private static final int PLANNING = 4;
    private static final int FIREBALL = 5;
    private static final int SCRATCHING = 6;


    public Player(TileMap tileMap) {
        super(tileMap);

        width = height = 30;
        cwidth = Creator.cwidth;
        cheight = Creator.cheight;
        moveSpeed = Creator.moveSpeed;
        maxSpeed = Creator.maxSpeed;
        stopSpeed = 0.4;
        fallSpeed = Creator.fallSpeed;
        maxFallSpeed = Creator.maxFallSpeed;
        jumpStart = Creator.jumpStart;
        jumpStopSpeed = 0.3;
        health = maxHealth = Creator.maxHealth;
        fire = maxFire = Creator.maxNumberOfShells;
        fireCost = Creator.shotPrice;
        fireDamage = Creator.shotDamage;
        restorationFire = Creator.restorationOfShells;
        fireballs = new ArrayList<Fireball>();
        scratchDamage = Creator.meleeDamage;
        scratchRange = Creator.meleeRange;
        numFrames = Creator.numFrames;
        FrameWidths = Creator.frameWidths;
        FrameHeights = Creator.frameHeights;
        SpriteDelays = Creator.spriteDelays;

        // load sprites
        try {
            int count = 0;
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream(Creator.playerAnimation));
            sprites = new ArrayList<BufferedImage[]>();
            for (int i = 0; i < 7; i++) {

                BufferedImage[] bufferedImages = new BufferedImage[numFrames[i]];
                for (int j = 0; j < numFrames[i]; j++) {
                    bufferedImages[j] = spritesheet.getSubimage(j * FrameWidths[i], count, FrameWidths[i], FrameHeights[i]);
                }
                sprites.add(bufferedImages);
                count += FrameHeights[i];
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        setAnimation(IDLE);

        Audio.load(Creator.sfxJump, "jump");
        Audio.load(Creator.sfxMelee, "scratch");
        Audio.load(Creator.sfxHit, "hit");
        Audio.load(Creator.sfxShot, "fire");
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
                    fireballs.get(j).setHit();
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
        if (!dead) {
            Audio.play("hit");
        }
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

    public void setAnimation(int action) {
        currentAction = action;
        animation.setFrames(sprites.get(currentAction));
        animation.setDelay(SpriteDelays[currentAction]);
        width = FrameWidths[currentAction];
        height = FrameHeights[currentAction];
    }

    public void update() {

        // position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemporary, ytemporary);
        if (y >= tileMap.getHeight() - 14) {
            dead = true;
            deadTimer = System.nanoTime();
        }

        // check attack has stopped
        if (currentAction == SCRATCHING) {
            if (animation.hasPlayedOnce()) scratching = false;
        }
        if (currentAction == FIREBALL) {
            if (animation.hasPlayedOnce()) firing = false;
        }

        // fireballs
        fire += restorationFire;
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
                Audio.play("scratch");
                setAnimation(SCRATCHING);
            }
        } else if (firing) {
            if (currentAction != FIREBALL) {
                Audio.play("fire");
                setAnimation(FIREBALL);
            }
        } else if (dy > 0) {
            if (planning) {
                if (currentAction != PLANNING) {
                    setAnimation(PLANNING);
                }
            } else if (falling) {
                if (currentAction != FALLING) {
                    setAnimation(FALLING);
                }
            }
        } else if (dy < 0) {
            if (currentAction != JUMPING) {
                Audio.play("jump");
                setAnimation(JUMPING);
            }
        } else if (left || right) {
            if (currentAction != WALKING) {
                setAnimation(WALKING);
            }
        } else if (currentAction != IDLE) {
            setAnimation(IDLE);
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