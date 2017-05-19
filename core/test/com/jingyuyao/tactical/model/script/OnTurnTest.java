package com.jingyuyao.tactical.model.script;

import static com.google.common.truth.Truth.assertThat;

import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.world.World;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OnTurnTest {

  @Mock
  private Turn turn1;
  @Mock
  private Turn turn2;
  @Mock
  private World world;

  @Test
  public void is_met() {
    assertThat(new OnTurn(turn1).isMet(turn1, world)).isTrue();
  }

  @Test
  public void is_not_met() {
    assertThat(new OnTurn(turn1).isMet(turn2, world)).isFalse();
  }
}