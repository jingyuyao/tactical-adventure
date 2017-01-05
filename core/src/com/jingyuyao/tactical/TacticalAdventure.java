package com.jingyuyao.tactical;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.jingyuyao.tactical.controller.ControllerModule;
import com.jingyuyao.tactical.controller.MapController;
import com.jingyuyao.tactical.data.DataModule;
import com.jingyuyao.tactical.data.MapLoader;
import com.jingyuyao.tactical.model.ModelModule;
import com.jingyuyao.tactical.view.MapScreen;
import com.jingyuyao.tactical.view.ViewModule;

public class TacticalAdventure extends Game {

  private Injector injector;

  @Override
  public void create() {
    injector =
        Guice.createInjector(
            // we need to pre-load all singletons so they can start receiving events
            Stage.PRODUCTION,
            new AssetModule(),
            new GameModule(),
            new ModelModule(),
            new DataModule(),
            new ViewModule(),
            new ControllerModule());
    injector.getInstance(EventBus.class).register(this);
    setLevel(AssetModule.TEST_MAP);
  }

  @Subscribe
  public void logDeadEvent(DeadEvent deadEvent) {
    Gdx.app.log("DeadEvent", deadEvent.getEvent().toString());
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
    injector.getInstance(MapLoader.class).loadMap(tiledMap);
    injector.getInstance(MapController.class).initiateControl();
    setScreen(injector.getInstance(MapScreen.class));
  }
}
