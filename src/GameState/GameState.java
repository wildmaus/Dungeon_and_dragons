package GameState;

public abstract class GameState {

    protected GameStateManager gsm;

    public abstract void update();
    public abstract void draw(java.awt.Graphics2D graphics2D);
    public abstract void keyPressed(int key);
    public abstract void keyReleased(int key);

}
