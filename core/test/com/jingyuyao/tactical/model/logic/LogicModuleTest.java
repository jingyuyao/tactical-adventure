package com.jingyuyao.tactical.model.logic;

import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.AttackPlanFactory;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LogicModuleTest {

  @Bind
  @Mock
  private AttackPlanFactory attackPlanFactory;
  @Inject
  private PassiveRetaliation passiveRetaliation;

  @Before
  public void setUp() {
    Guice.createInjector(BoundFieldModule.of(this), new LogicModule()).injectMembers(this);
  }

  @Test
  public void can_create_instances() {

  }
}