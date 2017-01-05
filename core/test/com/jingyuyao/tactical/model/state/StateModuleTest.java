package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.jingyuyao.tactical.model.AttackPlan;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.TargetsFactory;
import com.jingyuyao.tactical.model.mark.Markings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StateModuleTest {

  @Mock
  private Player player;
  @Mock
  private Enemy enemy;
  @Mock
  private AttackPlan attackPlan;
  @Mock
  private Markings markings;
  @Mock
  private TargetsFactory targetsFactory;

  private Injector injector;

  @Before
  public void setUp() {
    injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bind(Markings.class).toInstance(markings);
        bind(TargetsFactory.class).toInstance(targetsFactory);
      }
    }, new StateModule());
  }

  @Test
  public void state_factory() {
    StateFactory stateFactory = injector.getInstance(StateFactory.class);

    stateFactory.createWaiting();
    stateFactory.createMoving(player);
    stateFactory.createChoosing(player);
    stateFactory.createSelectingWeapon(player, enemy);
    stateFactory.createUsingItem(player);
    stateFactory.createReviewingAttack(player, attackPlan);
  }

  @Test
  public void map_state_singleton() {
    assertThat(injector.getInstance(MapState.class)).isSameAs(injector.getInstance(MapState.class));
  }
}