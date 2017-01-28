package com.jingyuyao.tactical;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Guice;
import com.jingyuyao.tactical.controller.ControllerModule;
import com.jingyuyao.tactical.controller.MapController;
import com.jingyuyao.tactical.data.DataModule;
import com.jingyuyao.tactical.data.MapLoader;
import com.jingyuyao.tactical.model.ModelModule;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.view.MapScreen;
import com.jingyuyao.tactical.view.MapUI;
import com.jingyuyao.tactical.view.MapView;
import com.jingyuyao.tactical.view.ViewModule;
import javax.inject.Inject;

public class TacticalAdventure extends Game {

  @Inject
  @ModelEventBus
  private EventBus modelEventBus;
  @Inject
  private MapScreen mapScreen;
  @Inject
  private MapUI mapUI;
  @Inject
  private MapView mapView;
  @Inject
  private MapLoader mapLoader;
  @Inject
  private MapController mapController;
  @Inject
  private Batch batch;
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
    modelEventBus.register(mapUI);
    modelEventBus.register(mapView);
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
    mapScreen.dispose();
    batch.dispose();
    assetManager.dispose();
  }

  public void setLevel(String mapName) {
    mapLoader.loadMap(mapName);
    setScreen(mapScreen);
    mapController.receiveInput();
  }
}
