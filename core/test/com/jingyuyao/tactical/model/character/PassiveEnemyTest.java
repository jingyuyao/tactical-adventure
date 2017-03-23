package com.jingyuyao.tactical.model.character;

import static org.mockito.Mockito.verify;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.Movements;
import com.jingyuyao.tactical.model.state.SelectionHandler;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
// TODO: test retaliation
public class PassiveEnemyTest {

  private static final Coordinate COORDINATE = new Coordinate(100, 100);
  private static final String NAME = "yo";
  private static final int MAX_HP = 20;
  private static final int HP = 10;
  private static final int MOVE_DISTANCE = 3;

  @Mock
  private EventBus eventBus;
  @Mock
  private List<Item> items;
  @Mock
  private Movements movements;
  @Mock
  private SelectionHandler selectionHandler;
  @Mock
  private Battle battle;

  private PassiveEnemy enemy;

  @Before
  public void setUp() {
    enemy =
        new PassiveEnemy(
            COORDINATE, movements, eventBus, battle, NAME, MAX_HP, HP, MOVE_DISTANCE, items);
  }

  @Test
  public void select() {
    enemy.select(selectionHandler);

    verify(selectionHandler).select(enemy);
  }
}