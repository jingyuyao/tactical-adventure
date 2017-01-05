package com.jingyuyao.tactical.model;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class ModelModuleTest {

  private Injector injector;

  @Before
  public void setUp() {
    injector = Guice.createInjector(new ModelModule());
  }

  @Test
  public void waiter_singleton() {
    Waiter waiter1 = injector.getInstance(Waiter.class);
    Waiter waiter2 = injector.getInstance(Waiter.class);
    assertThat(waiter1).isSameAs(waiter2);
  }

  @Test
  public void attackPlanFactory_singleton() {
    AttackPlanFactory factory1 = injector.getInstance(AttackPlanFactory.class);
    AttackPlanFactory factory2 = injector.getInstance(AttackPlanFactory.class);
    assertThat(factory1).isSameAs(factory2);
  }
}
