package com.jingyuyao.tactical.view.world2;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.view.world2.component.ComponentModule;
import com.jingyuyao.tactical.view.world2.system.LoopAnimationSystem;
import com.jingyuyao.tactical.view.world2.system.RenderSystem;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Qualifier;
import javax.inject.Singleton;

public class WorldModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(Batch.class);

    install(new ComponentModule());
  }

  @Provides
  @Singleton
  PooledEngine provideEngine(
      RenderSystem renderSystem,
      LoopAnimationSystem loopAnimationSystem) {
    PooledEngine engine = new PooledEngine();
    engine.addSystem(loopAnimationSystem);
    engine.addSystem(renderSystem);
    return engine;
  }

  @Provides
  @Singleton
  Map<Character, Entity> provideEntityMap() {
    return new HashMap<>();
  }

  @Provides
  @Singleton
  @WorldViewport
  Viewport provideWorldViewport(WorldConfig worldConfig) {
    return new ExtendViewport(worldConfig.getWorldViewportWidth(),
        worldConfig.getWorldViewportHeight());
  }

  /**
   * {@link com.badlogic.gdx.maps.tiled.TiledMap} must be set before the renderer can be used.
   */
  @Provides
  @Singleton
  OrthogonalTiledMapRenderer provideTiledMapRenderer(Batch batch, WorldConfig worldConfig) {
    return new OrthogonalTiledMapRenderer(null, worldConfig.getTileToWorldScale(), batch);
  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  public @interface WorldViewport {

  }
}
