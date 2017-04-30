package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jingyuyao.tactical.view.ui.WorldUIModule.UIStage;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldUI {

  private final Stage stage;
  private final LayerManager layerManager;
  private final MainLayer mainLayer;

  @Inject
  WorldUI(@UIStage Stage stage, LayerManager layerManager, MainLayer mainLayer) {
    this.stage = stage;
    this.layerManager = layerManager;
    this.mainLayer = mainLayer;
  }

  public InputProcessor getInputProcessor() {
    return stage;
  }

  public void init() {
    layerManager.init(mainLayer);
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
}
