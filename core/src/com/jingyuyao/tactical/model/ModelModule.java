package com.jingyuyao.tactical.model;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.model.battle.BattleModule;
import com.jingyuyao.tactical.model.character.CharacterModule;
import com.jingyuyao.tactical.model.item.ItemModule;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.MapModule;
import com.jingyuyao.tactical.model.state.StateModule;
import com.jingyuyao.tactical.model.terrain.TerrainModule;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Qualifier;
import javax.inject.Singleton;

public class ModelModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new MapModule());
    install(new CharacterModule());
    install(new BattleModule());
    install(new TerrainModule());
    install(new ItemModule());
    install(new StateModule());
  }

  @Provides
  @Singleton
  @ModelEventBus
  EventBus providesModelEventBus() {
    return new EventBus("model");
  }

  @Provides
  @Singleton
  @BackingCellMap
  Map<Coordinate, Cell> provideBackingCellMap() {
    return new HashMap<>();
  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  public @interface ModelEventBus {

  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface BackingCellMap {

  }
}
