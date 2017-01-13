package com.jingyuyao.tactical.model.common;

import com.google.inject.AbstractModule;

public class CommonModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Algorithms.class);
  }
}
