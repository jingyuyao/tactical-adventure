package com.jingyuyao.tactical;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Guice;
import com.jingyuyao.tactical.controller.ControllerModule;
import com.jingyuyao.tactical.data.DataModule;
import com.jingyuyao.tactical.data.MapLoader;
import com.jingyuyao.tactical.data.MapSaver;
import com.jingyuyao.tactical.model.ModelModule;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.view.ViewModule;
import com.jingyuyao.tactical.view.WorldScreen;
import com.jingyuyao.tactical.view.WorldScreenSubscribers;
import javax.inject.Inject;

public class TacticalAdventure extends Game {

  @Inject
  @ModelEventBus
  private EventBus modelEventBus;
  @Inject
  private WorldScreen worldScreen;
  @Inject
  private WorldScreenSubscribers worldScreenSubscribers;
  @Inject
  private MapLoader mapLoader;
  @Inject
  private MapSaver mapSaver;
  @Inject
  private AssetManager assetManager;

  @Override
  public void create() {
    Guice
        .createInjector(
            new AssetModule(),
            new ModelModule(),
            new DataModule(),
            new ViewModule(),
            new ControllerModule())
        .injectMembers(this);
    worldScreenSubscribers.register(modelEventBus);
    modelEventBus.register(this);
    setLevel(AssetModule.TEST_MAP);
  }

  @Subscribe
  public void logDeadEvent(DeadEvent deadEvent) {
    Gdx.app.log("DeadEvent", deadEvent.getEvent().toString());
  }

  @Override
  public void dispose() {
    super.dispose();
    mapSaver.saveMap(AssetModule.TEST_MAP);
    worldScreen.dispose();
    assetManager.dispose();
  }

  private void setLevel(String mapName) {
    mapLoader.loadMap(mapName);
    setScreen(worldScreen);
  }
}
