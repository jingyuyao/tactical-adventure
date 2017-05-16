package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.jingyuyao.tactical.model.ship.Player;
import com.jingyuyao.tactical.model.world.Coordinate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GameSaveTest {

  private static final Coordinate P2 = new Coordinate(3, 3);

  @Mock
  private LevelProgress levelProgress;
  @Mock
  private Player player1;
  @Mock
  private Player player2;

  private GameSave gameSave;

  @Before
  public void setUp() {
    gameSave = new GameSave();
  }

  @Test
  public void update() {
    when(levelProgress.getInactivePlayers()).thenReturn(ImmutableList.of(player1));
    when(levelProgress.getActivePlayers()).thenReturn(ImmutableMap.of(P2, player2));

    gameSave.update(levelProgress);

    assertThat(gameSave.getPlayers()).containsExactly(player2, player1).inOrder();

    // tests the previous changes are cleared
    gameSave.update(levelProgress);

    assertThat(gameSave.getPlayers()).containsExactly(player2, player1).inOrder();
  }
}