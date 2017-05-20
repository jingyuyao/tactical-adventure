package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.jingyuyao.tactical.model.Allegiance;
import com.jingyuyao.tactical.model.ship.Ship;
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
  private Ship ship1;
  @Mock
  private Ship ship2;

  private GameSave gameSave;

  @Before
  public void setUp() {
    gameSave = new GameSave();
  }

  @Test
  public void update() {
    when(levelProgress.getReservedPlayerShips()).thenReturn(ImmutableList.of(ship1));
    when(levelProgress.getShips()).thenReturn(ImmutableMap.of(P2, ship2));
    when(ship2.getAllegiance()).thenReturn(Allegiance.PLAYER);

    gameSave.update(levelProgress);

    assertThat(gameSave.getInactiveShips()).containsExactly(ship1, ship2).inOrder();

    // tests the previous changes are cleared
    gameSave.update(levelProgress);

    assertThat(gameSave.getInactiveShips()).containsExactly(ship1, ship2).inOrder();
  }
}