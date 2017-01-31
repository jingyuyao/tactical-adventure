package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Movement;
import com.jingyuyao.tactical.model.map.MovementFactory;
import com.jingyuyao.tactical.model.map.TerrainGraphs;
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
  private MovementFactory movementFactory;
  @Bind
  @Mock
  private Characters characters;
  @Bind
  @Mock
  private TerrainGraphs terrainGraphs;
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

  @Inject
  private StateFactory stateFactory;
  @Inject
  private MapState mapState;

  @Before
  public void setUp() {
    Guice.createInjector(BoundFieldModule.of(this), new StateModule()).injectMembers(this);
  }

  @Test
  public void state_factory() {
    stateFactory.createIgnoreInput();
    stateFactory.createWaiting();
    stateFactory.createMoving(player, movement);
    stateFactory.createMoved(player);
    stateFactory.createSelectingTarget(player, weapon, targets);
    stateFactory.createSelectingItem(player);
    stateFactory.createReviewingAttack(player, weapon, target);
    stateFactory.createRetaliating();
  }
}