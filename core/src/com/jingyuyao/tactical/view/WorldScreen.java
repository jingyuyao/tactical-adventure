package com.jingyuyao.tactical.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.jingyuyao.tactical.controller.WorldController;
import com.jingyuyao.tactical.view.marking.Markings;
import com.jingyuyao.tactical.view.resource.AnimationTime;
import com.jingyuyao.tactical.view.ui.UI;
import com.jingyuyao.tactical.view.world.WorldView;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldScreen extends ScreenAdapter {

  private final WorldView worldView;
  private final Markings markings;
  private final UI ui;
  private final AnimationTime animationTime;
  private final WorldController worldController;

  @Inject
  WorldScreen(
      WorldView worldView,
      Markings markings,
      UI ui,
      AnimationTime animationTime,
      WorldController worldController) {
    this.worldView = worldView;
    this.markings = markings;
    this.ui = ui;
    this.animationTime = animationTime;
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
    worldView.act(delta);
    animationTime.advanceStateTime(delta);

    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    worldView.draw();
    markings.draw();
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
    worldView.dispose();
    ui.dispose();
  }
}
