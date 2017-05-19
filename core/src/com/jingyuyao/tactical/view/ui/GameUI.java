package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.ModelBusListener;
import com.jingyuyao.tactical.model.event.WorldLoaded;
import com.jingyuyao.tactical.view.ui.GameUIModule.UIStage;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@ModelBusListener
public class GameUI {

  private final Stage stage;
  private final LayerManager layerManager;
  private final MainLayer mainLayer;

  @Inject
  GameUI(@UIStage Stage stage, LayerManager layerManager, MainLayer mainLayer) {
    this.stage = stage;
    this.layerManager = layerManager;
    this.mainLayer = mainLayer;
  }

  public InputProcessor getInputProcessor() {
    return stage;
  }

  public void act(float delta) {
    stage.act(delta);
  }

  public void draw() {
    stage.getViewport().apply();
    stage.draw();
  }

  public void resize(int width, int height) {
    stage.getViewport().update(width, height);
  }

  public void dispose() {
    stage.dispose();
  }

  @Subscribe
  void worldLoaded(WorldLoaded worldLoaded) {
    layerManager.init(mainLayer);
  }
}
