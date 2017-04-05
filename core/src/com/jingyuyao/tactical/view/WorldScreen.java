package com.jingyuyao.tactical.view;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.jingyuyao.tactical.controller.WorldController;
import com.jingyuyao.tactical.view.ui.UI;
import com.jingyuyao.tactical.view.world2.WorldView;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldScreen extends ScreenAdapter {

  private final GL20 gl;
  private final WorldView worldView;
  private final UI ui;
  private final WorldController worldController;

  @Inject
  WorldScreen(
      GL20 gl,
      WorldView worldView,
      UI ui,
      WorldController worldController) {
    this.gl = gl;
    this.worldView = worldView;
    this.ui = ui;
    this.worldController = worldController;
  }

  @Override
  public void show() {
    worldController.receiveInput();
  }

  @Override
  public void hide() {
    worldController.stopReceivingInput();
  }

  @Override
  public void render(float delta) {
    ui.act(delta);
    gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    worldView.update(delta);
    ui.draw();
  }

  @Override
  public void resize(int width, int height) {
    // This is very important...
    worldView.resize(width, height);
    ui.resize(width, height);
  }

  @Override
  public void dispose() {
    ui.dispose();
  }
}
