package com.jingyuyao.tactical.view.world;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.view.world.component.ComponentModule;
import com.jingyuyao.tactical.view.world.resource.ResourceModule;
import com.jingyuyao.tactical.view.world.system.SystemModule;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.inject.Qualifier;
import javax.inject.Singleton;

public class WorldViewModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(AssetManager.class);
    requireBinding(Batch.class);

    install(new ComponentModule());
    install(new ResourceModule());
    install(new SystemModule());
  }

  @Provides
  @Singleton
  @WorldEngine
  PooledEngine provideEngine() {
    return new PooledEngine();
  }

  @Provides
  @Singleton
  @WorldViewport
  ExtendViewport provideWorldViewport(WorldConfig worldConfig) {
    return new ExtendViewport(
        worldConfig.getWorldViewportWidth(), worldConfig.getWorldViewportHeight());
  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface WorldViewport {

  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface WorldEngine {

  }
}
