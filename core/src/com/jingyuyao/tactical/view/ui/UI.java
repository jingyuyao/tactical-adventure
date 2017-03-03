package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jingyuyao.tactical.view.ui.UIModule.UIStage;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UI {

  private final Stage stage;

  @Inject
  UI(@UIStage Stage stage, RootTable rootTable) {
    this.stage = stage;
    stage.addActor(rootTable);
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
