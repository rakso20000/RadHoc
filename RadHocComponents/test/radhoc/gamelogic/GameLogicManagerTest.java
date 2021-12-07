package radhoc.gamelogic;

import org.junit.jupiter.api.Test;
import radhoc.gamelogic.impl.GameLogicManagerImpl;
import radhoc.gamestates.GameState;
import radhoc.gamestates.GameType;
import radhoc.gamestates.impl.GameStateImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameLogicManagerTest {

    @Test
    void getGameLogicTets() {
        GameLogicManager glm = new GameLogicManagerImpl();
        GameState gms = new GameStateImpl(GameType.TIC_TAC_TOE, "Dieter", 123, 321);
        assertEquals(GameType.TIC_TAC_TOE, glm.getGameLogic(gms).getGameType());
    }
}
