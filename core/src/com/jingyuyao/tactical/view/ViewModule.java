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
import com.jingyuyao.tactical.view.MapUI.MapUiViewport;
import com.jingyuyao.tactical.view.MapView.MapViewViewport;
import com.jingyuyao.tactical.view.actor.ActorModule;
import javax.inject.Singleton;

public class ViewModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new ActorModule());

    bind(MapScreen.class);
    bind(MapView.class);
    bind(MapUI.class);
    bind(ViewConfig.class);
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
  @MapView.MapViewStage
  Stage provideMapViewStage(@MapViewViewport Viewport viewport, Batch batch) {
    return new Stage(viewport, batch);
  }

  @Provides
  @Singleton
  @MapViewViewport
  Viewport provideMapViewViewport(ViewConfig viewConfig) {
    return new ExtendViewport(
        viewConfig.getViewportWorldWidth(), viewConfig.getViewportWorldHeight());
  }

  /**
   * {@link com.badlogic.gdx.maps.tiled.TiledMap} must be set before the renderer can be used.
   */
  @Provides
  @Singleton
  OrthogonalTiledMapRenderer provideTiledMapRenderer(ViewConfig viewConfig, Batch batch) {
    return new OrthogonalTiledMapRenderer(null, viewConfig.getTileToWorldScale(), batch);
  }

  @Provides
  @Singleton
  @MapUI.MapUiStage
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
