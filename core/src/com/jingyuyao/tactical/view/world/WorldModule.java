package com.jingyuyao.tactical.view.world;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.controller.ControllerFactory;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.view.actor.ActorFactory;
import com.jingyuyao.tactical.view.actor.WorldActor;
import com.jingyuyao.tactical.view.resource.Animations;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Qualifier;
import javax.inject.Singleton;

public class WorldModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(AssetManager.class);
    requireBinding(Batch.class);
    requireBinding(ControllerFactory.class);
    requireBinding(ActorFactory.class);
    requireBinding(Animations.class);

    bind(Group.class).annotatedWith(CharacterGroup.class).to(Group.class).in(Singleton.class);
    bind(Group.class).annotatedWith(TerrainGroup.class).to(Group.class).in(Singleton.class);
  }

  @Provides
  @Singleton
  @WorldStage
  Stage provideWorldStage(Batch batch, @WorldViewport Viewport viewport) {
    return new Stage(viewport, batch);
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

  @Provides
  @Singleton
  @BackingActorMap
  Map<MapObject, WorldActor<?>> provideActorMap() {
    return new HashMap<>();
  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  public @interface WorldStage {

  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  public @interface WorldViewport {

  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface CharacterGroup {

  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface TerrainGroup {

  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface BackingActorMap {

  }
}
