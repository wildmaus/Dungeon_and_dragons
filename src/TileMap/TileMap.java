package TileMap;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileMap {

    // position
    private double x;
    private double y;

    // bounds
    private int xmin;
    private int xmax;
    private int ymin;
    private int ymax;

    private double tween;

    // map
    private int[][] map;
    private int tileSize;
    private int numRows;
    private int numColoms;
    private int width;
    private int height;

    // tileset
    private BufferedImage tileset;
    private int numTilesAcross;
    private Tile[][] tiles;

    // drawing
    private int rowOffset;
    private int colOffset;
    private int numRowsToDraw;
    private int numColsToDraw;

    public TileMap(int tileSize) {

        this.tileSize = tileSize;
        numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
        numColsToDraw = GamePanel.WIDTH / tileSize + 2;
        tween = 1;

    }

    public void loadTiles(String s) {

        try {
            tileset = ImageIO.read(getClass().getResourceAsStream(s));
            numTilesAcross = tileset.getWidth() / tileSize;
            tiles = new Tile[2][numTilesAcross];
            BufferedImage subimage;
            for (int col = 0; col < numTilesAcross; col++) {
                subimage = tileset.getSubimage(col * tileSize, 0, tileSize, tileSize);
                tiles[0][col] = new Tile(subimage, Tile.NORMAL);
                subimage = tileset.getSubimage(col * tileSize, tileSize, tileSize, tileSize);
                tiles[1][col] = new Tile(subimage, Tile.BLOCKED);

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void loadMap(String s){

        try {
            InputStream inputStream = getClass().getResourceAsStream(s);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            numColoms = Integer.parseInt(bufferedReader.readLine());
            numRows = Integer.parseInt(bufferedReader.readLine());
            map = new int[numRows][numColoms];
            width = numColoms * tileSize;
            height = numRows * tileSize;
            xmin = GamePanel.WIDTH - width;
            xmax = 0;
            ymin = GamePanel.HEIGHT - height;
            ymax = 0;

            String delimeters = "\\s+";
            for (int row = 0; row < numRows; row ++) {
                String line = bufferedReader.readLine();
                String[] tokens = line.split(delimeters);
                for (int col = 0; col < numColoms; col ++) {
                    map[row][col] = Integer.parseInt(tokens[col]);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int getTileSize() {return tileSize;}
    public int getx() {return (int)x;}
    public int gety () {return (int)y;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}

    public int getType(int row, int col) {
        int rowCol = map[row][col];
        int r = rowCol / numTilesAcross;
        int c = rowCol % numTilesAcross;
        return tiles[r][c].getType();
    }

    private void fixBounds() {
        if (x < xmin) x = xmin;
        if (x > xmax) x = xmax;
        if (y < ymin) y = ymin;
        if (y > ymax) y = ymax;
    }

    public void setTween(double tween){
        this.tween = tween;
    }

    public void setPosition(double x, double y) {

        this.x += (x - this.x) * tween;
        this.y += (y - this.y) * tween;
        fixBounds();
        colOffset = (int)-this.x / tileSize;
        rowOffset = (int)-this.y / tileSize;
    }

    public void draw(Graphics2D graphics2D) {
        for (int row = rowOffset; row < rowOffset + numRowsToDraw; row ++) {
            if (row >= numRows) break;

            for (int col = colOffset; col < colOffset + numColsToDraw; col ++) {
                if (col >= numColoms) break;

                if (map[row][col] == 0) continue;
                int rowCol = map[row][col];
                int r = rowCol / numTilesAcross;
                int c = rowCol % numTilesAcross;
                graphics2D.drawImage(tiles[r][c].getImage(),(int)x + col * tileSize, (int)y + row * tileSize, null);

            }

        }
    }
}
