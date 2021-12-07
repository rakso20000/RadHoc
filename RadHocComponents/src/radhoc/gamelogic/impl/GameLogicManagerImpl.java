package radhoc.gamelogic.impl;

import radhoc.gamelogic.GameLogic;
import radhoc.gamelogic.GameLogicManager;
import radhoc.gamestates.GameState;
import radhoc.gamestates.GameType;

import java.util.HashMap;
import java.util.Map;

public class GameLogicManagerImpl implements GameLogicManager {

    private Map<GameState, GameLogic> gameLogics  = new HashMap<>();

    @Override
    public GameLogic getGameLogic(GameState state) {
        if(gameLogics.get(state) != null) {
            return gameLogics.get(state);
        } else {
            GameType gt = state.getGameType();
            GameLogic gl;
            switch (gt) {
                //TODO different GameTypes
                default:
                    gl = new GameLogicTicTacToeImpl(state);
                    gameLogics.put(state, gl);
                    break;
            }

            return gl;
        }
    }
}
