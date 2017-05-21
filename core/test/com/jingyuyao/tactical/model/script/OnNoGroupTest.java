package com.jingyuyao.tactical.model.script;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.ship.ShipGroup;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OnNoGroupTest {

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

  private OnNoGroup onNoGroup;

  @Before
  public void setUp() {
    onNoGroup = new OnNoGroup(ShipGroup.PLAYER);
  }

  @Test
  public void satisfied() {
    when(world.getShipSnapshot()).thenReturn(ImmutableMap.of(cell1, ship1, cell2, ship2));
    when(ship1.getGroup()).thenReturn(ShipGroup.ENEMY);
    when(ship2.getGroup()).thenReturn(ShipGroup.ENEMY);

    assertThat(onNoGroup.onTurn(turn, world)).isTrue();
    assertThat(onNoGroup.onDeath(person, world)).isTrue();
  }

  @Test
  public void not_satisfied() {
    when(world.getShipSnapshot()).thenReturn(ImmutableMap.of(cell1, ship1, cell2, ship2));
    when(ship1.getGroup()).thenReturn(ShipGroup.PLAYER);

    assertThat(onNoGroup.onTurn(turn, world)).isFalse();
    assertThat(onNoGroup.onDeath(person, world)).isFalse();
  }
}