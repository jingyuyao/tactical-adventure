package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Terrain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class IgnoreInputTest {

  @Mock
  private MapState mapState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private Player player;
  @Mock
  private Enemy enemy;
  @Mock
  private Terrain terrain;

  private IgnoreInput ignoreInput;

  @Before
  public void setUp() {
    ignoreInput = new IgnoreInput(mapState, stateFactory);
  }

  @Test
  public void exit() {
    ignoreInput.exit();

    verify(mapState).removeLast();
  }

  @Test
  public void selects() {
    ignoreInput.select(player);
    ignoreInput.select(enemy);
    ignoreInput.select(terrain);

    verifyZeroInteractions(mapState);
    verifyZeroInteractions(player);
    verifyZeroInteractions(enemy);
    verifyZeroInteractions(terrain);
  }

  @Test
  public void actions() {
    assertThat(ignoreInput.getActions()).isEmpty();
  }
}