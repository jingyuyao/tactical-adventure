package com.jingyuyao.tactical.model.character;

import static org.mockito.Mockito.verify;

import com.google.common.collect.Multiset;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.Marker;
import com.jingyuyao.tactical.model.map.Movements;
import com.jingyuyao.tactical.model.state.MapState;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
// TODO: test retaliation
public class PassiveEnemyTest {

  private static final Coordinate CHARACTER_COORDINATE = new Coordinate(100, 100);
  private static final String NAME = "yo";
  private static final int MAX_HP = 20;
  private static final int HP = 10;
  private static final int MOVE_DISTANCE = 3;

  @Mock
  private Multiset<Marker> markers;
  @Mock
  private EventBus eventBus;
  @Mock
  private List<Item> items;
  @Mock
  private Movements movements;
  @Mock
  private Characters characters;
  @Mock
  private MapState mapState;

  private PassiveEnemy enemy;

  @Before
  public void setUp() {
    enemy =
        new PassiveEnemy(CHARACTER_COORDINATE, markers, movements, characters, eventBus, NAME,
            MAX_HP, HP, MOVE_DISTANCE, items);
  }

  @Test
  public void select() {
    enemy.select(mapState);

    verify(mapState).select(enemy);
  }
}