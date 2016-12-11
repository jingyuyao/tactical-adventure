package com.jingyuyao.tactical;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jingyuyao.tactical.screen.GameScreen;

public class TacticalAdventure extends Game {
    private AssetManager assetManager;
	private SpriteBatch batch;
	
	@Override
	public void create () {
        assetManager = Assets.load();
		batch = new SpriteBatch();

		setScreen(new GameScreen(this));
	}

	@Override
	public void dispose () {
		super.dispose();
        assetManager.dispose();
		batch.dispose();
	}

    public AssetManager getAssetManager() {
        return assetManager;
    }

	public SpriteBatch getBatch() {
		return batch;
	}
}
