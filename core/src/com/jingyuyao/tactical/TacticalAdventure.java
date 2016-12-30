package com.jingyuyao.tactical;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.jingyuyao.tactical.controller.ControllerModule;
import com.jingyuyao.tactical.controller.MapController;
import com.jingyuyao.tactical.data.DataModule;
import com.jingyuyao.tactical.data.LevelLoader;
import com.jingyuyao.tactical.model.ModelModule;
import com.jingyuyao.tactical.view.MapScreen;
import com.jingyuyao.tactical.view.ViewModule;

public class TacticalAdventure extends Game {
    private Injector injector;

    @Override
    public void create() {
        injector = Guice.createInjector(
                new AssetModule(),
                new GameModule(),
                new ModelModule(),
                new DataModule(),
                new ViewModule(),
                new ControllerModule()
        );
        setLevel(AssetModule.TEST_MAP);
    }

    @Override
    public void dispose() {
        super.dispose();
        injector.getInstance(MapScreen.class).dispose();
        injector.getInstance(Batch.class).dispose();
        injector.getInstance(AssetManager.class).dispose();
    }

    public void setLevel(String mapName) {
        TiledMap tiledMap = injector.getInstance(AssetManager.class).get(mapName, TiledMap.class);
        injector.getInstance(LevelLoader.class).loadLevel(tiledMap);
        injector.getInstance(MapController.class).initiateControl();
        setScreen(injector.getInstance(MapScreen.class));
    }
}
