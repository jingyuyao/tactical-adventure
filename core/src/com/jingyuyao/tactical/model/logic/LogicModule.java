package com.jingyuyao.tactical.model.logic;

import com.google.inject.AbstractModule;
import com.jingyuyao.tactical.model.AttackPlanFactory;

public class LogicModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(AttackPlanFactory.class);

    bind(PassiveRetaliation.class);
  }
}
