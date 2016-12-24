package com.jingyuyao.tactical;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.jingyuyao.tactical.controller.LevelController;
import com.jingyuyao.tactical.data.LevelLoader;
import com.jingyuyao.tactical.model.Level;
import com.jingyuyao.tactical.view.LevelScreen;
import com.jingyuyao.tactical.view.LevelScreenFactory;

public class TacticalAdventure extends Game {
    private AssetManager assetManager;
    private LevelScreenFactory levelScreenFactory;

    @Override
    public void create() {
        assetManager = Assets.createAssetManager();
        levelScreenFactory = new LevelScreenFactory(this, assetManager);

        setLevel(Assets.TEST_MAP);
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.dispose();
    }

    public void setLevel(String mapName) {
        TiledMap tiledMap = assetManager.get(mapName, TiledMap.class);
        Level level = LevelLoader.loadLevel(tiledMap);
        LevelScreen levelScreen = levelScreenFactory.createScreen(level, tiledMap);
        LevelController.initiateControl(levelScreen, level);
        setScreen(levelScreen);
    }
}
