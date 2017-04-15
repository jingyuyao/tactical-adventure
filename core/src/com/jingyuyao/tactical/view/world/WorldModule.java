package com.jingyuyao.tactical.view.world;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.controller.WorldCamera;
import com.jingyuyao.tactical.controller.WorldController;
import com.jingyuyao.tactical.view.world.component.ComponentModule;
import com.jingyuyao.tactical.view.world.resource.ResourceModule;
import com.jingyuyao.tactical.view.world.system.SystemModule;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.inject.Qualifier;
import javax.inject.Singleton;

public class WorldModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(Batch.class);
    requireBinding(Graphics.class);
    requireBinding(WorldController.class);
    requireBinding(WorldCamera.class);

    install(new ComponentModule());
    install(new ResourceModule());
    install(new SystemModule());
  }

  @Provides
  @Singleton
  Engine provideEngine() {
    return new PooledEngine();
  }

  @Provides
  @Singleton
  @WorldViewport
  Viewport provideWorldViewport(WorldConfig worldConfig, Graphics graphics) {
    Viewport viewport =
        new ExtendViewport(
            worldConfig.getWorldViewportWidth(), worldConfig.getWorldViewportHeight());
    viewport.update(graphics.getWidth(), graphics.getHeight(), true);
    return viewport;
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
