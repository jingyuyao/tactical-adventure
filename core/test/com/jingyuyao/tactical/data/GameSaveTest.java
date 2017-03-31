package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;

import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GameSaveTest {

  private static final Coordinate P1 = new Coordinate(1, 1);
  private static final Coordinate P2 = new Coordinate(2, 2);
  private static final Coordinate E1 = new Coordinate(3, 3);
  private static final Coordinate E2 = new Coordinate(4, 4);

  @Mock
  private Player player1;
  @Mock
  private Player player2;
  @Mock
  private Enemy enemy1;
  @Mock
  private Enemy enemy2;

  private GameSave gameSave;

  @Before
  public void setUp() {
    gameSave = new GameSave();
  }

  @Test
  public void get_active_characters() {
    Map<Coordinate, Player> activePlayers = gameSave.getActivePlayers();
    Map<Coordinate, Enemy> activeEnemies = gameSave.getActiveEnemies();
    activePlayers.put(P1, player1);
    activePlayers.put(P2, player2);
    activeEnemies.put(E1, enemy1);
    activeEnemies.put(E2, enemy2);

    assertThat(gameSave.getActiveCharacters())
        .containsExactly(
            P1, player1,
            P2, player2,
            E1, enemy1,
            E2, enemy2);
  }
}