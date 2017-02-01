package com.jingyuyao.tactical.view;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Singleton;

public class ViewModule extends AbstractModule {

  private static final int TILE_SIZE = 32; // pixels
  private static final float TILE_TO_WORLD_SCALE = 1f / TILE_SIZE;
  private static final int VIEWPORT_WORLD_WIDTH = 16;
  private static final int VIEWPORT_WORLD_HEIGHT = 9;

  @Override
  protected void configure() {
    install(new ActorModule());
  }

  @Provides
  @Singleton
  Batch provideBatch() {
    return new SpriteBatch();
  }

  @Provides
  @Singleton
  Skin provideSkin(AssetManager assetManager) {
    return assetManager.get(AssetModule.SKIN, Skin.class);
  }

  @Provides
  @Singleton
  Map<MapObject, MapActor<?>> provideActorMap() {
    return new HashMap<MapObject, MapActor<?>>();
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
    return new ExtendViewport(VIEWPORT_WORLD_WIDTH, VIEWPORT_WORLD_HEIGHT);
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
  @MapUiStage
  Stage provideMapUIStage(@MapUiViewport Viewport viewport, Batch batch) {
    return new Stage(viewport, batch);
  }

  @Provides
  @Singleton
  @MapUiViewport
  Viewport provideMapUIViewport() {
    return new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
  }

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  public @interface MapUiStage {

  }

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  public @interface MapUiViewport {

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
}
