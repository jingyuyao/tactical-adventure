package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.view.ui.WorldUIModule.UIStage;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldUI {

  private final Stage stage;
  private final WorldUILayout worldUiLayout;

  @Inject
  WorldUI(@UIStage Stage stage, WorldUILayout worldUiLayout) {
    this.stage = stage;
    this.worldUiLayout = worldUiLayout;
    this.stage.addActor(worldUiLayout.rootTable());
  }

  public void register(ModelBus modelBus) {
    worldUiLayout.register(modelBus);
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
}
