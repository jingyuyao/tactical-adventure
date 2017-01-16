package com.jingyuyao.tactical.model.logic;

import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.AttackPlanFactory;
import com.jingyuyao.tactical.model.retaliation.RetaliationModule;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RetaliationModuleTest {

  @Bind
  @Mock
  private AttackPlanFactory attackPlanFactory;
  @Inject
  private com.jingyuyao.tactical.model.retaliation.PassiveRetaliation passiveRetaliation;

  @Before
  public void setUp() {
    Guice.createInjector(BoundFieldModule.of(this), new RetaliationModule()).injectMembers(this);
  }

  @Test
  public void can_create_instances() {

  }
}