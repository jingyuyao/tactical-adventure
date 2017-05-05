package com.jingyuyao.tactical.model.state;

import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movement;
import com.jingyuyao.tactical.model.world.Movements;
import com.jingyuyao.tactical.model.world.World;
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
  private WorldState worldState;

  @Before
  public void setUp() {
    Guice.createInjector(BoundFieldModule.of(this), new StateModule()).injectMembers(this);
  }

  @Test
  public void state_factory() {
    when(cell.player()).thenReturn(Optional.of(player));

    stateFactory.createTransition();
    stateFactory.createWaiting();
    stateFactory.createMoving(cell, movement);
    stateFactory.createMoved(cell);
    stateFactory.createSelectingTarget(cell, weapon, targets);
    stateFactory.createUsingConsumable(cell, consumable);
    stateFactory.createBattling(cell, weapon, target);
    stateFactory.createRetaliating();
  }
}