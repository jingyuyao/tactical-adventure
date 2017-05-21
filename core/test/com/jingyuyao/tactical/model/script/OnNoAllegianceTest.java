package com.jingyuyao.tactical.model.script;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import com.jingyuyao.tactical.model.Allegiance;
import com.jingyuyao.tactical.model.person.Person;
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
public class OnNoAllegianceTest {

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
  @Mock
  private Person person;

  private OnNoAllegiance onNoAllegiance;

  @Before
  public void setUp() {
    onNoAllegiance = new OnNoAllegiance(Allegiance.PLAYER);
  }

  @Test
  public void satisfied() {
    when(world.getShipSnapshot()).thenReturn(ImmutableMap.of(cell1, ship1, cell2, ship2));
    when(ship1.getAllegiance()).thenReturn(Allegiance.ENEMY);
    when(ship2.getAllegiance()).thenReturn(Allegiance.ENEMY);

    assertThat(onNoAllegiance.onTurn(turn, world)).isTrue();
    assertThat(onNoAllegiance.onDeath(person, world)).isTrue();
  }

  @Test
  public void not_satisfied() {
    when(world.getShipSnapshot()).thenReturn(ImmutableMap.of(cell1, ship1, cell2, ship2));
    when(ship1.getAllegiance()).thenReturn(Allegiance.PLAYER);

    assertThat(onNoAllegiance.onTurn(turn, world)).isFalse();
    assertThat(onNoAllegiance.onDeath(person, world)).isFalse();
  }
}