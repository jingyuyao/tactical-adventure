package com.jingyuyao.tactical.view;

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
import com.google.inject.Provides;
import com.jingyuyao.tactical.AssetModule;
import com.jingyuyao.tactical.view.ViewAnnotations.MapUiStage;
import com.jingyuyao.tactical.view.ViewAnnotations.MapUiViewport;
import com.jingyuyao.tactical.view.ViewAnnotations.MapViewStage;
import com.jingyuyao.tactical.view.ViewAnnotations.MapViewViewport;
import com.jingyuyao.tactical.view.actor.ActorModule;
import javax.inject.Singleton;

public class ViewModule extends AbstractModule {

  private static final int TILE_SIZE = 32; // pixels
  private static final float TILE_TO_WORLD_SCALE = 1f / TILE_SIZE;
  private static final int VIEWPORT_WORLD_WIDTH = 15;
  private static final int VIEWPORT_WORLD_HEIGHT = 10;

  @Override
  protected void configure() {
    if (!currentStage().equals(com.google.inject.Stage.PRODUCTION)) {
      addError("ViewModule requires Stage.PRODUCTION for singleton pre-loading.");
    }

    install(new ActorModule());

    bind(MapScreen.class);
    bind(MapView.class);
    bind(MapUI.class);
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
}
