package com.jingyuyao.tactical.model.state;

import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.World;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.Movement;
import com.jingyuyao.tactical.model.map.Movements;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StateModuleTest {

  @Bind
  @Mock
  @ModelEventBus
  private EventBus eventBus;
  @Bind
  @Mock
  private World world;
  @Bind
  @Mock
  private Battle battle;
  @Bind
  @Mock
  private Movements movements;
  @Mock
  private Cell cell;
  @Mock
  private Player player;
  @Mock
  private Weapon weapon;
  @Mock
  private ImmutableList<Target> targets;
  @Mock
  private Target target;
  @Mock
  private Movement movement;
  @Mock
  private Consumable consumable;

  @Inject
  private StateFactory stateFactory;
  @Inject
  private MapState mapState;
  @Inject
  private SelectionHandler selectionHandler;

  @Before
  public void setUp() {
    Guice.createInjector(BoundFieldModule.of(this), new StateModule()).injectMembers(this);
  }

  @Test
  public void state_factory() {
    when(cell.hasPlayer()).thenReturn(true);
    when(cell.getPlayer()).thenReturn(player);

    stateFactory.createTransition();
    stateFactory.createWaiting();
    stateFactory.createMoving(cell, movement);
    stateFactory.createMoved(cell);
    stateFactory.createSelectingTarget(player, weapon, targets);
    stateFactory.createUsingConsumable(player, consumable);
    stateFactory.createBattling(player, weapon, target);
    stateFactory.createRetaliating();
  }
}