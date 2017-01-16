package com.jingyuyao.tactical.model.retaliation;

import com.google.inject.AbstractModule;

public class RetaliationModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(PassiveRetaliation.class);
  }
}
