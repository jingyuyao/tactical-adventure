package com.jingyuyao.tactical;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.jingyuyao.tactical.controller.LevelController;
import com.jingyuyao.tactical.data.DataModule;
import com.jingyuyao.tactical.data.LevelLoader;
import com.jingyuyao.tactical.model.Level;
import com.jingyuyao.tactical.model.ModelModule;
import com.jingyuyao.tactical.view.LevelScreen;
import com.jingyuyao.tactical.view.LevelScreenFactory;

public class TacticalAdventure extends Game {
    private AssetManager assetManager;
    private LevelScreenFactory levelScreenFactory;
    private Injector injector;

    @Override
    public void create() {
        injector = Guice.createInjector(
                new GameModule(),
                new ModelModule(),
                new DataModule()
        );

        assetManager = Assets.createAssetManager();
        levelScreenFactory = new LevelScreenFactory(injector.getInstance(EventBus.class), assetManager);

        setLevel(Assets.TEST_MAP);
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.dispose();
    }

    public void setLevel(String mapName) {
        TiledMap tiledMap = assetManager.get(mapName, TiledMap.class);
        LevelLoader levelLoader = injector.getInstance(LevelLoader.class);
        levelLoader.loadLevel(tiledMap);
        Level level = injector.getInstance(Level.class);
        LevelScreen levelScreen = levelScreenFactory.createScreen(level, tiledMap);
        LevelController.initiateControl(levelScreen, level);
        setScreen(levelScreen);
    }
}
