package com.jingyuyao.tactical;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.jingyuyao.tactical.controller.LevelController;
import com.jingyuyao.tactical.view.LevelScreen;
import com.jingyuyao.tactical.view.LevelScreenFactory;

public class TacticalAdventure extends Game {
    private AssetManager assetManager;
    private LevelScreenFactory levelScreenFactory;

    @Override
    public void create() {
        assetManager = Assets.createAssetManager();
        levelScreenFactory = new LevelScreenFactory(this, assetManager);

        setLevelScreen(Assets.TEST_MAP);
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.dispose();
    }

    public void setLevelScreen(String mapName) {
        LevelScreen levelScreen = levelScreenFactory.create(mapName);
        LevelController.initiateControl(levelScreen);
        setScreen(levelScreen);
    }
}
