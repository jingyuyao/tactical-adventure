package com.jingyuyao.tactical.model.script;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.resource.ResourceKey;
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
public class DiedTest {

  @Mock
  private Turn turn;
  @Mock
  private World world;
  @Mock
  private Cell cell1;
  @Mock
  private Ship ship1;
  @Mock
  private Person crew1;
  @Mock
  private ResourceKey key1;

  private Died died;

  @Before
  public void setUp() {
    died = new Died("me");
  }

  @Test
  public void is_met() {
    when(world.getShipSnapshot()).thenReturn(ImmutableMap.of(cell1, ship1));
    when(ship1.getCrew()).thenReturn(ImmutableList.of(crew1));
    when(crew1.getName()).thenReturn(key1);
    when(key1.getId()).thenReturn("you");

    assertThat(died.isMet(turn, world)).isTrue();
  }

  @Test
  public void is_not_met() {
    when(world.getShipSnapshot()).thenReturn(ImmutableMap.of(cell1, ship1));
    when(ship1.getCrew()).thenReturn(ImmutableList.of(crew1));
    when(crew1.getName()).thenReturn(key1);
    when(key1.getId()).thenReturn("me");

    assertThat(died.isMet(turn, world)).isFalse();
  }
}