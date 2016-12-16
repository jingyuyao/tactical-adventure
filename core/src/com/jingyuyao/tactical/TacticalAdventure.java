package com.jingyuyao.tactical;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.jingyuyao.tactical.controller.ControllerModule;
import com.jingyuyao.tactical.model.ModelModule;
import com.jingyuyao.tactical.screen.GameScreenFactory;
import com.jingyuyao.tactical.screen.ScreenModule;
import com.jingyuyao.tactical.view.ViewModule;

public class TacticalAdventure extends Game {
    private Injector injector;
    private GameScreenFactory gameScreenFactory;

    @Override
    public void create() {
        injector = Guice.createInjector(
                new AssetsModule(),
                new GameModule(this),
                new ScreenModule(),
                new ViewModule(),
                new ControllerModule(),
                new ModelModule()
        );
        gameScreenFactory = injector.getInstance(GameScreenFactory.class);

        setGameScreen(AssetsModule.TEST_MAP);
    }

    @Override
    public void dispose() {
        super.dispose();
        injector.getInstance(AssetManager.class).dispose();
    }

    public void setGameScreen(String mapName) {
        setScreen(gameScreenFactory.create(mapName));
    }
}
