package com.jingyuyao.tactical.model.script;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.resource.ResourceKey;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.world.World;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OnDeathTest {

  @Mock
  private Ship ship1;
  @Mock
  private Ship ship2;
  @Mock
  private Person dead1;
  @Mock
  private ResourceKey name1;
  @Mock
  private Person dead2;
  @Mock
  private ResourceKey name2;
  @Mock
  private Turn turn;
  @Mock
  private World world;

  @Test
  public void conditions() {
    when(ship1.getCrew()).thenReturn(ImmutableList.of(dead1));
    when(ship2.getCrew()).thenReturn(ImmutableList.of(dead2));
    when(dead1.getName()).thenReturn(name1);
    when(name1.getId()).thenReturn("me");
    when(dead2.getName()).thenReturn(name2);
    when(name2.getId()).thenReturn("you");

    OnDeath onDeath = new OnDeath("me");

    assertThat(onDeath.onShipDestroyed(ship1, world)).isTrue();
    assertThat(onDeath.onShipDestroyed(ship2, world)).isFalse();
    assertThat(onDeath.onTurn(turn, world)).isFalse();
  }
}