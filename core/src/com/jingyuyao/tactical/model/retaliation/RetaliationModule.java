package com.jingyuyao.tactical.model.retaliation;

import com.google.inject.AbstractModule;
import com.jingyuyao.tactical.model.AttackPlanFactory;

public class RetaliationModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(AttackPlanFactory.class);

    bind(PassiveRetaliation.class);
  }
}
