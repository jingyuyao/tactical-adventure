package com.jingyuyao.tactical.model.script;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
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
public class AllDiedTest {

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
  private Person crew1;
  @Mock
  private Person crew2;
  @Mock
  private ResourceKey key1;
  @Mock
  private ResourceKey key2;

  private AllDied allDied;

  @Before
  public void setUp() {
    allDied = new AllDied(ImmutableSet.of("me", "you"));
  }

  @Test
  public void is_met() {
    when(world.getShipSnapshot()).thenReturn(ImmutableList.of(cell1, cell2));
    when(cell1.ship()).thenReturn(Optional.of(ship1));
    when(cell2.ship()).thenReturn(Optional.of(ship2));
    when(ship1.getCrew()).thenReturn(ImmutableList.of(crew1));
    when(ship2.getCrew()).thenReturn(ImmutableList.of(crew2));
    when(crew1.getName()).thenReturn(key1);
    when(crew2.getName()).thenReturn(key2);
    when(key1.getId()).thenReturn("guess");
    when(key2.getId()).thenReturn("what");

    assertThat(allDied.isMet(turn, world)).isTrue();
  }

  @Test
  public void is_not_met() {
    when(world.getShipSnapshot()).thenReturn(ImmutableList.of(cell1, cell2));
    when(cell1.ship()).thenReturn(Optional.of(ship1));
    when(cell2.ship()).thenReturn(Optional.of(ship2));
    when(ship1.getCrew()).thenReturn(ImmutableList.of(crew1));
    when(ship2.getCrew()).thenReturn(ImmutableList.of(crew2));
    when(crew1.getName()).thenReturn(key1);
    when(crew2.getName()).thenReturn(key2);
    when(key1.getId()).thenReturn("me");
    when(key2.getId()).thenReturn("is alive");

    assertThat(allDied.isMet(turn, world)).isFalse();
  }
}