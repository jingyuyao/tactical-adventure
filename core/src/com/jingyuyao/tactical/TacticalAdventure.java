package com.jingyuyao.tactical;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.jingyuyao.tactical.controller.ControllerModule;
import com.jingyuyao.tactical.data.DataModule;
import com.jingyuyao.tactical.data.ModelLoader;
import com.jingyuyao.tactical.data.ModelSaver;
import com.jingyuyao.tactical.model.ModelModule;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.view.ViewModule;
import com.jingyuyao.tactical.view.WorldScreen;
import com.jingyuyao.tactical.view.WorldScreenSubscribers;
import javax.inject.Inject;

public class TacticalAdventure extends Game {

  static final String TEST_MAP = "test_map";

  @Inject
  @ModelEventBus
  private EventBus modelEventBus;
  @Inject
  private GameSubscriber gameSubscriber;
  @Inject
  private WorldScreen worldScreen;
  @Inject
  private WorldScreenSubscribers worldScreenSubscribers;
  @Inject
  private ModelLoader modelLoader;
  @Inject
  private ModelSaver modelSaver;
  @Inject
  private AssetManager assetManager;

  @Override
  public void create() {
    Guice
        .createInjector(
            new GameModule(this),
            new ModelModule(),
            new DataModule(),
            new ViewModule(),
            new ControllerModule())
        .injectMembers(this);

    modelEventBus.register(gameSubscriber);
    worldScreenSubscribers.register(modelEventBus);
    setLevel(TEST_MAP);
  }

  @Override
  public void pause() {
    super.pause();
    modelSaver.saveMap(TEST_MAP);
  }

  @Override
  public void dispose() {
    super.dispose();
    worldScreen.dispose();
    assetManager.dispose();
  }

  void setLevel(String mapName) {
    modelLoader.loadMap(mapName);
    setScreen(worldScreen);
  }
}
