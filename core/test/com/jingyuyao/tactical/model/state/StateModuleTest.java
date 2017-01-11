package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;

import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.AttackPlan;
import com.jingyuyao.tactical.model.AttackPlanFactory;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
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
  private AttackPlanFactory attackPlanFactory;
  @Mock
  private Player player;
  @Mock
  private Enemy enemy;
  @Mock
  private AttackPlan attackPlan;
  @Inject
  private MapState mapState1;
  @Inject
  private MapState mapState2;
  @Inject
  private StateFactory stateFactory;

  @Before
  public void setUp() {
    Guice.createInjector(BoundFieldModule.of(this), new StateModule()).injectMembers(this);
  }

  @Test
  public void state_factory() {
    stateFactory.createWaiting();
    stateFactory.createMoving(player);
    stateFactory.createChoosing(player);
    stateFactory.createSelectingWeapon(player, enemy);
    stateFactory.createUsingItem(player);
    stateFactory.createReviewingAttack(player, enemy, attackPlan);
  }

  @Test
  public void map_state_singleton() {
    assertThat(mapState1).isSameAs(mapState2);
  }
}