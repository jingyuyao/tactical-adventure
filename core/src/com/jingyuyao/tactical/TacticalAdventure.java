package com.jingyuyao.tactical;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.jingyuyao.tactical.screen.GameScreen;
import com.jingyuyao.tactical.screen.ScreenModule;

public class TacticalAdventure extends Game {
	private Injector injector;
	
	@Override
	public void create () {
	    injector = Guice.createInjector(new AssetsModule(), new GameModule(this), new ScreenModule());

		setScreen(injector.getInstance(GameScreen.class));
	}

	@Override
	public void dispose () {
		super.dispose();
		injector.getInstance(AssetManager.class).dispose();
	}

    /**
     * Simple module to bind {@link TacticalAdventure}
     */
    private static class GameModule extends AbstractModule {
        private final TacticalAdventure game;

        private GameModule(TacticalAdventure game) {
            this.game = game;
        }

        @Override
        protected void configure() {
            bind(TacticalAdventure.class).toInstance(game);
        }
    }
}
