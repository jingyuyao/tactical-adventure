package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.terrain.Terrain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TransitionTest {

  @Mock
  private MapState mapState;
  @Mock
  private Player player;
  @Mock
  private Enemy enemy;
  @Mock
  private Terrain terrain;

  private Transition transition;

  @Before
  public void setUp() {
    transition = new Transition(mapState);
  }

  @Test
  public void exit() {
    transition.exit();

    verify(mapState).popLast();
  }

  @Test
  public void selects() {
    transition.select(player);
    transition.select(enemy);
    transition.select(terrain);

    verifyZeroInteractions(mapState);
    verifyZeroInteractions(player);
    verifyZeroInteractions(enemy);
    verifyZeroInteractions(terrain);
  }

  @Test
  public void actions() {
    assertThat(transition.getActions()).isEmpty();
  }
}