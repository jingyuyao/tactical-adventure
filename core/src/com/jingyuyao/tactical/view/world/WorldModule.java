package com.jingyuyao.tactical.view.world;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.AssetModule;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.view.actor.WorldActor;
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
  }

  @Provides
  @Singleton
  @WorldStage
  Stage provideWorldStage(@WorldViewport Viewport viewport, Batch batch) {
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

  @Provides
  @Singleton
  @CharacterSprites
  Map<String, Sprite> provideNameSpriteMap(AssetManager assetManager) {
    Map<String, Sprite> nameSpriteMap = new HashMap<>();
    nameSpriteMap.put("jingyu", new Sprite(assetManager.get(AssetModule.JINGYU, Texture.class)));
    nameSpriteMap.put("andrew", new Sprite(assetManager.get(AssetModule.ANDREW, Texture.class)));
    nameSpriteMap.put("kevin", new Sprite(assetManager.get(AssetModule.KEVIN, Texture.class)));
    nameSpriteMap.put("ben", new Sprite(assetManager.get(AssetModule.BEN, Texture.class)));
    nameSpriteMap.put("soldier", new Sprite(assetManager.get(AssetModule.SOLDIER, Texture.class)));
    return nameSpriteMap;
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
  @interface BackingActorMap {

  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface CharacterSprites {

  }
}
