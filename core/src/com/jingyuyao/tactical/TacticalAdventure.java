package com.jingyuyao.tactical;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.jingyuyao.tactical.screen.GameScreenFactory;

public class TacticalAdventure extends Game {
    private AssetManager assetManager;
    private GameScreenFactory gameScreenFactory;

    @Override
    public void create() {
        assetManager = Assets.createAssetManager();
        gameScreenFactory = new GameScreenFactory(this, assetManager);

        setGameScreen(Assets.TEST_MAP);
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.dispose();
    }

    public void setGameScreen(String mapName) {
        setScreen(gameScreenFactory.create(mapName));
    }
}
