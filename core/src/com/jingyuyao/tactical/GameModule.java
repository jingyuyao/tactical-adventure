package com.jingyuyao.tactical;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Singleton;

class GameModule extends AbstractModule {
  @Override
  protected void configure() {}

  @Provides
  @Singleton
  EventBus provideEventBus() {
    return new EventBus("game");
  }
}
