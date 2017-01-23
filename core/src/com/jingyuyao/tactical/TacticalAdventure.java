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
import com.google.inject.Stage;
import com.jingyuyao.tactical.controller.ControllerModule;
import com.jingyuyao.tactical.data.DataModule;
import com.jingyuyao.tactical.data.MapLoader;
import com.jingyuyao.tactical.model.ModelModule;
import com.jingyuyao.tactical.view.MapScreen;
import com.jingyuyao.tactical.view.ViewModule;
import javax.inject.Inject;

public class TacticalAdventure extends Game {

  @Inject
  private EventBus eventBus;
  @Inject
  private MapScreen mapScreen;
  @Inject
  private Batch batch;
  @Inject
  private AssetManager assetManager;
  @Inject
  private MapLoader mapLoader;

  @Override
  public void create() {
    Guice
        .createInjector(
            // we need to pre-load all singletons so they can start receiving events
            Stage.PRODUCTION,
            new AssetModule(),
            new GameModule(),
            new ModelModule(),
            new DataModule(),
            new ViewModule(),
            new ControllerModule())
        .injectMembers(this);
    eventBus.register(this);
    setLevel(AssetModule.TEST_MAP);
  }

  @Subscribe
  public void logDeadEvent(DeadEvent deadEvent) {
    Gdx.app.log("DeadEvent", deadEvent.getEvent().toString());
  }

  @Override
  public void dispose() {
    super.dispose();
    mapScreen.dispose();
    batch.dispose();
    assetManager.dispose();
  }

  public void setLevel(String mapName) {
    TiledMap tiledMap = assetManager.get(mapName, TiledMap.class);
    mapLoader.loadMap(tiledMap);
    setScreen(mapScreen);
  }
}
