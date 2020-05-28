package GameState;

import java.awt.*;

public class GameStateManager {

    private GameState[] gameStates;
    private int currentState;

    public static final int NUMOFSTATES = 2;
    public static final int MENUSTATE = 0;
    public static final int LEVEL1STATE = 1;
    public GameStateManager() {

        gameStates = new GameState[NUMOFSTATES];

        currentState = MENUSTATE;
        loadState(currentState);

    }

    private void loadState(int state) {
        if (state == MENUSTATE) {
            gameStates[state] = new MenuState(this);
        }
        if (state == LEVEL1STATE) {
            gameStates[state] = new Level1State(this);
        }
    }

    private void unloadState(int state) {
        gameStates[state] = null;
    }

    public void setState(int state) {
        unloadState(currentState);
        currentState = state;
        loadState(currentState);
    }

    public  void update() {
        try {
        gameStates[currentState].update();
        }
        catch (Exception e) {
        }
    }


    public void draw(Graphics2D graphics2D) {
        try {
            gameStates[currentState].draw(graphics2D);
        }
        catch (Exception e) {
        }
    }

    public void keyPressed(int key) {
        gameStates[currentState].keyPressed(key);
    }

    public void keyReleased(int key) {
        gameStates[currentState].keyReleased(key);
    }
}
