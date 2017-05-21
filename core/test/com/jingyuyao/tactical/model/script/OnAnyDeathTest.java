package com.jingyuyao.tactical.model.script;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.resource.ResourceKey;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.world.World;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OnAnyDeathTest {

  @Mock
  private Person dead1;
  @Mock
  private ResourceKey name1;
  @Mock
  private Person dead2;
  @Mock
  private ResourceKey name2;
  @Mock
  private Person dead3;
  @Mock
  private ResourceKey name3;
  @Mock
  private Turn turn;
  @Mock
  private World world;

  @Test
  public void conditions() {
    when(dead1.getName()).thenReturn(name1);
    when(name1.getId()).thenReturn("me");
    when(dead2.getName()).thenReturn(name2);
    when(name2.getId()).thenReturn("you");
    when(dead3.getName()).thenReturn(name3);
    when(name3.getId()).thenReturn("them");

    OnAnyDeath onAnyDeath = new OnAnyDeath(ImmutableSet.of("me", "you"));

    assertThat(onAnyDeath.onDeath(dead1, world)).isTrue();
    assertThat(onAnyDeath.onDeath(dead2, world)).isTrue();
    assertThat(onAnyDeath.onDeath(dead3, world)).isFalse();
    assertThat(onAnyDeath.onTurn(turn, world)).isFalse();
  }
}