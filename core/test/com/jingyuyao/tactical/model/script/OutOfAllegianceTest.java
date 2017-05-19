package com.jingyuyao.tactical.model.script;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.Allegiance;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OutOfAllegianceTest {

  @Mock
  private Turn turn;
  @Mock
  private World world;
  @Mock
  private Cell cell1;
  @Mock
  private Cell cell2;
  @Mock
  private Ship ship1;
  @Mock
  private Ship ship2;

  private OutOfAllegiance outOfAllegiance;

  @Before
  public void setUp() {
    outOfAllegiance = new OutOfAllegiance(Allegiance.PLAYER);
  }

  @Test
  public void is_met() {
    when(world.getShipSnapshot()).thenReturn(ImmutableList.of(cell1, cell2));
    when(cell1.ship()).thenReturn(Optional.of(ship1));
    when(cell2.ship()).thenReturn(Optional.of(ship2));
    when(ship1.getAllegiance()).thenReturn(Allegiance.ENEMY);
    when(ship2.getAllegiance()).thenReturn(Allegiance.ENEMY);

    assertThat(outOfAllegiance.isMet(turn, world)).isTrue();
  }

  @Test
  public void is_not_met() {
    when(world.getShipSnapshot()).thenReturn(ImmutableList.of(cell1, cell2));
    when(cell1.ship()).thenReturn(Optional.of(ship1));
    when(ship1.getAllegiance()).thenReturn(Allegiance.PLAYER);

    assertThat(outOfAllegiance.isMet(turn, world)).isFalse();
  }
}