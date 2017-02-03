package com.jingyuyao.tactical.view;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Provides;
import com.jingyuyao.tactical.AssetModule;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.view.actor.ActorModule;
import com.jingyuyao.tactical.view.actor.MapActor;
import com.jingyuyao.tactical.view.ui.RootTable;
import com.jingyuyao.tactical.view.ui.UIModule;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Singleton;

public class ViewModule extends AbstractModule {

  private static final int TILE_SIZE = 32; // pixels
  private static final float TILE_TO_WORLD_SCALE = 1f / TILE_SIZE;
  private static final int WORLD_WIDTH = 16;
  private static final int WORLD_HEIGHT = 9;
  private static final int UI_WORLD_SCALE = 50;

  @Override
  protected void configure() {
    install(new ActorModule());
    install(new UIModule());
  }

  @Provides
  @Singleton
  Batch provideBatch() {
    return new SpriteBatch();
  }

  @Provides
  @Singleton
  Map<MapObject, MapActor<?>> provideActorMap() {
    return new HashMap<>();
  }

  @Provides
  @Singleton
  @MapViewStage
  Stage provideMapViewStage(@MapViewViewport Viewport viewport, Batch batch) {
    return new Stage(viewport, batch);
  }

  @Provides
  @Singleton
  @MapViewViewport
  Viewport provideMapViewViewport() {
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
  @Singleton
  @MapUIStage
  Stage provideMapUIStage(@MapUIViewport Viewport viewport, Batch batch, RootTable rootTable) {
    Stage stage = new Stage(viewport, batch);
    stage.addActor(rootTable);
    return stage;
  }

  @Provides
  @Singleton
  @MapUIViewport
  Viewport provideMapUIViewport() {
    return new StretchViewport(WORLD_WIDTH * UI_WORLD_SCALE, WORLD_HEIGHT * UI_WORLD_SCALE);
  }

  @Provides
  @Singleton
  @MapMarkingsActionActor
  Actor provideMapMarkingsActionActor() {
    return new Actor();
  }

  @Provides
  @Singleton
  Map<Marker, Sprite> provideMarkerSpriteMap(AssetManager assetManager) {
    Map<Marker, Sprite> markerSpriteMap = new HashMap<>();
    markerSpriteMap.put(
        Marker.CAN_MOVE_TO, new Sprite(assetManager.get(AssetModule.MOVE, Texture.class)));
    markerSpriteMap.put(
        Marker.HIT, new Sprite(assetManager.get(AssetModule.HIT, Texture.class)));
    markerSpriteMap.put(
        Marker.CAN_ATTACK, new Sprite(assetManager.get(AssetModule.ATTACK, Texture.class)));
    markerSpriteMap.put(
        Marker.TARGET_SELECT,
        new Sprite(assetManager.get(AssetModule.TARGET_SELECT, Texture.class)));
    return markerSpriteMap;
  }

  @Provides
  @Singleton
  @HighlightSprite
  Sprite provideHighlightSprite(AssetManager assetManager) {
    return new Sprite(assetManager.get(AssetModule.HIGHLIGHT, Texture.class));
  }

  @Provides
  @Singleton
  @ActivatedCharacterSprite
  Sprite provideActivatedCharacterSprite(AssetManager assetManager) {
    return new Sprite(assetManager.get(AssetModule.ACTIVATED, Texture.class));
  }

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface HighlightSprite {

  }

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface ActivatedCharacterSprite {

  }

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  public @interface MapUIStage {

  }

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface MapUIViewport {

  }

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  public @interface MapViewStage {

  }

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  public @interface MapViewViewport {

  }

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface MapMarkingsActionActor {

  }
}
