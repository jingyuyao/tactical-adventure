package com.jingyuyao.tactical.data;

import com.google.inject.AbstractModule;

public class DataModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(MapLoader.class);
  }
}
