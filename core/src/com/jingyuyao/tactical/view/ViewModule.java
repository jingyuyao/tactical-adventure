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
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.AssetModule;
import com.jingyuyao.tactical.view.actor.ActorModule;

import javax.inject.Singleton;

public class ViewModule extends AbstractModule {
    private static final int TILE_SIZE = 32; // pixels
    private static final float RENDER_SCALE = 1f / TILE_SIZE;
    private static final int VIEWPORT_WIDTH = 15; // # tiles
    private static final int VIEWPORT_HEIGHT = 10; // # tiles

    @Override
    protected void configure() {
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
    @MapView.MapViewStage
    Stage provideMapViewStage(Batch batch) {
        return new Stage(new ExtendViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT), batch);
    }

    /**
     * {@link com.badlogic.gdx.maps.tiled.TiledMap} must be set before the renderer can be used.
     */
    @Provides
    @Singleton
    OrthogonalTiledMapRenderer provideTiledMapRenderer(Batch batch) {
        return new OrthogonalTiledMapRenderer(null, RENDER_SCALE, batch);
    }

    @Provides
    @Singleton
    @MapUI.MapUiStage
    Stage provideMapUIStage(Batch batch) {
        // why no constructor with only batch...
        return new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), batch);
    }
}
