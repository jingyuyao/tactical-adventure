package com.jingyuyao.tactical.model.world;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.jingyuyao.tactical.model.ModelBus;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Qualifier;
import javax.inject.Singleton;

public class WorldModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(ModelBus.class);

    install(new FactoryModuleBuilder().build(CellFactory.class));
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
  @interface BackingCellMap {

  }
}