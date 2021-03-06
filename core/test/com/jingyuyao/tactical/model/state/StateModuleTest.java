package com.jingyuyao.tactical.model.state;

import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movement;
import com.jingyuyao.tactical.model.world.World;
import java.util.Collections;
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
  private ModelBus modelBus;
  @Bind
  @Mock
  private World world;
  @Mock
  private Cell cell;
  @Mock
  private Ship ship;
  @Mock
  private Weapon weapon;
  @Mock
  private Battle battle;
  @Mock
  private Movement movement;
  @Mock
  private Consumable consumable;

  @Inject
  private StateFactory stateFactory;
  @Inject
  private WorldState worldState;

  @Before
  public void setUp() {
    Guice.createInjector(BoundFieldModule.of(this), new StateModule()).injectMembers(this);
  }

  @Test
  public void state_factory() {
    when(cell.ship()).thenReturn(Optional.of(ship));
    when(ship.isControllable()).thenReturn(true);

    stateFactory.createTransition();
    stateFactory.createWaiting();
    stateFactory.createMoving(cell, movement);
    stateFactory.createMoved(cell);
    stateFactory.createSelectingTarget(cell, weapon, Collections.<Target>emptyList());
    stateFactory.createUsingConsumable(cell, consumable);
    stateFactory.createBattling(cell, battle);
    stateFactory.createRetaliating();
  }
}