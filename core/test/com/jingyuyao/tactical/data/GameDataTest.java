package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.world.Coordinate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GameDataTest {

  private static final Coordinate P2 = new Coordinate(3, 3);

  @Mock
  private LevelProgress levelProgress;
  @Mock
  private Ship player1;
  @Mock
  private Ship player2;

  private GameData gameData;

  @Before
  public void setUp() {
    gameData = new GameData();
  }

  @Test
  public void update() {
    when(levelProgress.getReservedPlayerShips()).thenReturn(ImmutableList.of(player1));
    when(levelProgress.getPlayerShips()).thenReturn(ImmutableMap.of(P2, player2));

    gameData.update(levelProgress);

    assertThat(gameData.getPlayerShips()).containsExactly(player2, player1).inOrder();

    // tests the previous changes are cleared
    gameData.update(levelProgress);

    assertThat(gameData.getPlayerShips()).containsExactly(player2, player1).inOrder();
  }
}