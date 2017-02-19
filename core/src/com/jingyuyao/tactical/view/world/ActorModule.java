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
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.jingyuyao.tactical.AssetModule;
import com.jingyuyao.tactical.model.map.MapObject;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import javax.inject.Qualifier;
import javax.inject.Singleton;

public class ActorModule extends AbstractModule {

  public static final int WORLD_WIDTH = 16;
  public static final int WORLD_HEIGHT = 9;
  private static final int TILE_SIZE = 32; // pixels
  private static final float TILE_TO_WORLD_SCALE = 1f / TILE_SIZE;

  @Override
  protected void configure() {
    install(new FactoryModuleBuilder().build(ActorFactory.class));
  }

  @Provides
  @Singleton
  Map<MapObject, MapActor<?>> provideActorMap() {
    return new HashMap<>();
  }

  @Provides
  @Singleton
  @MapActorsStage
  Stage provideMapActorsStage(@MapActorsViewport Viewport viewport, Batch batch) {
    return new Stage(viewport, batch);
  }

  @Provides
  @Singleton
  @MapActorsViewport
  Viewport provideMapActorsViewport() {
    return new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT);
  }

  /**
   * {@link com.badlogic.gdx.maps.tiled.TiledMap} must be set before the renderer can be used.
   */
  @Provides
  @Singleton
  OrthogonalTiledMapRenderer provideTiledMapRenderer(Batch batch) {
    return new OrthogonalTiledMapRenderer(null, TILE_TO_WORLD_SCALE, batch);
  }

  @Provides
  @InitialMarkers
  LinkedHashSet<Sprite> provideInitialMarkers() {
    return new LinkedHashSet<>();
  }

  @Provides
  @Singleton
  Map<String, Sprite> provideNameSpriteMap(AssetManager assetManager) {
    Map<String, Sprite> nameSpriteMap = new HashMap<>();
    nameSpriteMap.put("jingyu", new Sprite(assetManager.get(AssetModule.JINGYU, Texture.class)));
    nameSpriteMap.put("andrew", new Sprite(assetManager.get(AssetModule.ANDREW, Texture.class)));
    nameSpriteMap.put("kevin", new Sprite(assetManager.get(AssetModule.KEVIN, Texture.class)));
    nameSpriteMap.put("ben", new Sprite(assetManager.get(AssetModule.BEN, Texture.class)));
    nameSpriteMap.put("soldier", new Sprite(assetManager.get(AssetModule.SOLDIER, Texture.class)));
    return nameSpriteMap;
  }

  @Provides
  @ActorWorldSize
  float provideActorWorldSize() {
    return 1f;
  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  public @interface ActorWorldSize {

  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface InitialMarkers {

  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  public @interface MapActorsStage {

  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  public @interface MapActorsViewport {

  }
}
