package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.common.EventSubscriber;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Movement;
import com.jingyuyao.tactical.model.map.MovementFactory;
import java.util.Set;
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
  @Inject
  private Set<EventSubscriber> eventSubscribers;

  @Before
  public void setUp() {
    Guice.createInjector(BoundFieldModule.of(this), new StateModule()).injectMembers(this);
  }

  @Test
  public void state_factory() {
    stateFactory.createWaiting();
    stateFactory.createMoving(player, movement);
    stateFactory.createMoved(player);
    stateFactory.createSelectingTarget(player, weapon, targets);
    stateFactory.createSelectingItem(player);
    stateFactory.createReviewingAttack(player, weapon, target);
    stateFactory.createRetaliating();
  }

  @Test
  public void event_subscriber() {
    assertThat(eventSubscribers).containsExactly(mapState);
  }
}