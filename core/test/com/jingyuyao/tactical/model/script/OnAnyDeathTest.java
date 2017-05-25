package com.jingyuyao.tactical.model.script;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.resource.StringKey;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.world.World;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OnAnyDeathTest {

  @Mock
  private Ship ship1;
  @Mock
  private Ship ship2;
  @Mock
  private Ship ship3;
  @Mock
  private Person dead1;
  @Mock
  private StringKey name1;
  @Mock
  private Person dead2;
  @Mock
  private StringKey name2;
  @Mock
  private Person dead3;
  @Mock
  private StringKey name3;
  @Mock
  private Turn turn;
  @Mock
  private World world;

  @Test
  public void conditions() {
    when(ship1.getCrew()).thenReturn(Collections.singletonList(dead1));
    when(ship2.getCrew()).thenReturn(Arrays.asList(dead2, dead3));
    when(ship3.getCrew()).thenReturn(Collections.singletonList(dead2));
    when(dead1.getName()).thenReturn(name1);
    when(name1.getId()).thenReturn("me");
    when(dead2.getName()).thenReturn(name2);
    when(name2.getId()).thenReturn("them");
    when(dead3.getName()).thenReturn(name3);
    when(name3.getId()).thenReturn("you");

    OnAnyDeath onAnyDeath = new OnAnyDeath(new HashSet<>(Arrays.asList("me", "you")));

    assertThat(onAnyDeath.onShipDestroyed(ship1, world)).isTrue();
    assertThat(onAnyDeath.onShipDestroyed(ship2, world)).isTrue();
    assertThat(onAnyDeath.onShipDestroyed(ship3, world)).isFalse();
    assertThat(onAnyDeath.onTurn(turn, world)).isFalse();
  }
}