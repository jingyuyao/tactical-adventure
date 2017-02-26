package com.jingyuyao.tactical.model.character;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.Terrains;
import com.jingyuyao.tactical.model.state.MapState;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BasePlayerTest {

  private static final Coordinate CHARACTER_COORDINATE = new Coordinate(100, 100);
  private static final String NAME = "yo";
  private static final int MAX_HP = 20;
  private static final int HP = 10;
  private static final int MOVE_DISTANCE = 3;

  @Mock
  private List<Item> items;
  @Mock
  private EventBus eventBus;
  @Mock
  private Terrains terrains;
  @Mock
  private MapState mapState;
  private Player player;

  @Before
  public void setUp() {
    player =
        new BasePlayer(
            CHARACTER_COORDINATE, eventBus, terrains, NAME, MAX_HP, HP, MOVE_DISTANCE, items, true);
  }

  @Test
  public void select() {
    player.select(mapState);

    verify(mapState).select(player);
  }

  @Test
  public void get_actionable() {
    assertThat(player.isActionable()).isTrue();
  }

  @Test
  public void set_actionable() {
    player.setActionable(false);

    assertThat(player.isActionable()).isFalse();
  }
}