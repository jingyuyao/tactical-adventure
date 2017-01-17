package com.jingyuyao.tactical.model.retaliation;

import com.google.inject.AbstractModule;
import com.jingyuyao.tactical.model.map.MovementFactory;

public class RetaliationModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(MovementFactory.class);

    bind(PassiveRetaliation.class);
  }
}
