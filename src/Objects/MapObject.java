package Objects;

import Main.GamePanel;
import TileMap.TileMap;
import TileMap.Tile;

import java.awt.*;

public abstract class MapObject {

    // tiles
    protected TileMap tileMap;
    protected int tileSize;
    protected double xmap;
    protected double ymap;

    // position and vector
    protected double x;
    protected double y;
    protected double dx;
    protected double dy;

    // dimensions
    protected int width;
    protected int height;

    // collision box
    protected int cwidth;
    protected int cheight;

    // collision
    protected int currentRow;
    protected int currentCol;
    protected double xdestination;
    protected double ydestination;
    protected double xtemporary;
    protected double ytemporary;
    protected boolean topLeft;
    protected boolean topRight;
    protected boolean bottomLeft;
    protected boolean bottomRight;

    // animation
    protected Animation animation;
    protected int currentAction;
    protected int previousAction;
    protected boolean facingRight;

    // movement
    protected boolean left;
    protected boolean right;
    protected boolean up;
    protected boolean down;
    protected boolean jumping;
    protected boolean falling;

    // movement attributes
    protected double moveSpeed;
    protected double maxSpeed;
    protected double stopSpeed;
    protected double fallSpeed;
    protected double maxFallSpeed;
    protected double jumpStart;
    protected double jumpStopSpeed;

    // constructor
    public MapObject(TileMap tileMap) {
        this.tileMap = tileMap;
        tileSize = tileMap.getTileSize();
    }

    public boolean intersects(MapObject object) {
        Rectangle r1 = getRectangle();
        Rectangle r2 = object.getRectangle();
        return r1.intersects(r2);
    }

    public  Rectangle getRectangle() {
        return new Rectangle((int)x - cwidth, (int) y - cheight, cwidth, cheight);
    }

    public void calculateCorners(double x, double y) {
        int leftTile = (int) (x - cwidth / 2) / tileSize;
        int rightTile = (int) (x + cwidth / 2 - 1) / tileSize;
        int topTile = (int) (y - cheight / 2) / tileSize;
        int bottomTile = (int) (y + cheight / 2 - 1) / tileSize;

        int tl = tileMap.getType(topTile, leftTile);
        int tr = tileMap.getType(topTile, rightTile);
        int bl = tileMap.getType(bottomTile, leftTile);
        int br = tileMap.getType(bottomTile, rightTile);

        topLeft = tl == Tile.BLOCKED;
        topRight = tr == Tile.BLOCKED;
        bottomLeft = bl == Tile.BLOCKED;
        bottomRight = br == Tile.BLOCKED;

    }

    public void checkTileMapCollision() {

        currentCol = (int) x / tileSize;
        currentRow = (int) y / tileSize;

        xdestination = x + dx;
        ydestination = y + dy;

        xtemporary = x;
        ytemporary = y;

        calculateCorners(x, ydestination);
        if (dy < 0) {
            if (topRight || topLeft) {
                dy = 0;
                ytemporary = currentRow * tileSize + cheight / 2;
            }
            else {ytemporary += dy;}
        }
        if (dy > 0) {
            if (bottomRight || bottomLeft) {
                dy = 0;
                falling = false;
                ytemporary = (currentRow + 1) * tileSize - cheight / 2;
            }
            else {ytemporary += dy;}
        }

        calculateCorners(xdestination, y);
        if (dx < 0) {
            if (topRight || topLeft) {
                dx = 0;
                xtemporary = currentCol * tileSize + cwidth / 2;
            }
            else {xtemporary += dx;}
        }
        if (dx > 0) {
            if (bottomRight || bottomLeft) {
                dx = 0;
                xtemporary = (currentCol + 1) * tileSize - cwidth / 2;
            }
            else {xtemporary += dx;}
        }
        if (!falling) {
            calculateCorners(x, ydestination + 1);
            if (!bottomLeft && !bottomRight) {
                falling = true;
            }
        }

    }

    public int getx() {return (int)x;}
    public int gety() {return (int)y;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}
    public int getCWidth() {return cwidth;}
    public int getCHeight() {return cheight;}

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setVector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void setMapPosition() {
        xmap = tileMap.getx();
        ymap = tileMap.gety();
    }

    public void setLeft(boolean b) {left = b;}
    public void setRight(boolean b) {right = b;}
    public void setUp(boolean b) {up = b;}
    public void setDown(boolean b) {down = b;}
    public void setJumping(boolean b) {jumping = b;}

    public boolean notOnScrean() {
        return x + xmap + width < 0 || x + xmap - width > GamePanel.WIDTH || y + ymap + height < 0 || y + ymap - height > GamePanel.HEIGHT;
    }

    public void draw(Graphics2D graphics2D) {
        if (facingRight) {
            graphics2D.drawImage(animation.getImage(), (int) (x + xmap - width / 2), (int) (y + ymap - height / 2), null);
        } else {
            graphics2D.drawImage(animation.getImage(), (int) (x + xmap - width / 2 + width), (int) (y + ymap - height / 2), -width, height, null);
        }
    }
}
