package com.jingyuyao.tactical.model;

import static com.google.common.truth.Truth.assertThat;

import com.google.inject.CreationException;
import com.google.inject.Guice;
import com.google.inject.Stage;
import com.jingyuyao.tactical.model.common.Waiter;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;

public class ModelModuleTest {

  @Inject
  private Waiter waiter1;
  @Inject
  private Waiter waiter2;
  @Inject
  private AttackPlanFactory attackPlanFactory1;
  @Inject
  private AttackPlanFactory attackPlanFactory2;

  @Before
  public void setUp() {
    Guice.createInjector(Stage.PRODUCTION, new ModelModule()).injectMembers(this);
  }

  @Test(expected = CreationException.class)
  public void singleton_pre_loading_develop() {
    Guice.createInjector(Stage.DEVELOPMENT, new ModelModule()).injectMembers(this);
  }

  @Test(expected = CreationException.class)
  public void singleton_pre_loading_tool() {
    Guice.createInjector(Stage.TOOL, new ModelModule()).injectMembers(this);
  }

  @Test
  public void waiter_singleton() {
    assertThat(waiter1).isSameAs(waiter2);
  }

  @Test
  public void attackPlanFactory_singleton() {
    assertThat(attackPlanFactory1).isSameAs(attackPlanFactory2);
  }
}
