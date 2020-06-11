package GameState;

import Audio.Audio;
import Handler.Creator;
import Main.GamePanel;

import java.awt.*;

public class GameStateManager {

    private GameState[] gameStates;
    private int currentState;

    public static final int NUMOFSTATES = Creator.NumLevels + 1;
    public static final int MENUSTATE = 0;
    public static final int LEVEL1STATE = 1;

    public GameStateManager() {

        Audio.init();

        gameStates = new GameState[NUMOFSTATES];

        currentState = MENUSTATE;
        loadState(currentState);

    }

    private void loadState(int state) {
        if (state == MENUSTATE) {
            gameStates[state] = new MenuState(this);
        }
        else if (state < NUMOFSTATES) {
            gameStates[state] = new Level1State(this, Creator.TileSet[state - 1], Creator.Map[state - 1],
                    Creator.Bacground[state - 1], Creator.BgMusic[state - 1], Creator.Positions[state - 1],
                    Creator.PositionsEx[state - 1], Creator.PositionnsEy[state - 1], Creator.EyAsplus[state - 1]);
        }
    }

    private void unloadState(int state) {
        gameStates[state] = null;
    }

    public void setState(int state) {
        unloadState(currentState);
        if (state == NUMOFSTATES) {
            currentState = MENUSTATE;
        }
        else currentState = state;
        loadState(currentState);
    }

    public int getState() { return currentState;}

    public  void update() {
        if(gameStates[currentState] != null) gameStates[currentState].update();
    }


    public void draw(Graphics2D graphics2D) {
        if(gameStates[currentState] != null) gameStates[currentState].draw(graphics2D);
        else {
            graphics2D.setColor(java.awt.Color.BLACK);
            graphics2D.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
        }
    }

    public void keyPressed(int key) {
        if(gameStates[currentState] != null) {
            gameStates[currentState].keyPressed(key);
        }
    }

    public void keyReleased(int key) {
        if(gameStates[currentState] != null) {
            gameStates[currentState].keyReleased(key);
        }
    }
}
