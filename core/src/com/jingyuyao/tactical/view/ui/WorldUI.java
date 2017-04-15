package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.view.ui.WorldUIModule.UIStage;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldUI {

  private final Stage stage;
  private final UILayout uiLayout;

  @Inject
  WorldUI(@UIStage Stage stage, UILayout uiLayout) {
    this.stage = stage;
    this.uiLayout = uiLayout;
    this.stage.addActor(uiLayout);
  }

  public void register(EventBus eventBus) {
    uiLayout.register(eventBus);
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
