package com.jingyuyao.tactical.view;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.jingyuyao.tactical.controller.CameraController;
import com.jingyuyao.tactical.controller.WorldController;
import com.jingyuyao.tactical.view.ui.GameUI;
import com.jingyuyao.tactical.view.world.WorldView;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GameScreen extends ScreenAdapter {

  private final GL20 gl;
  private final Input input;
  private final WorldView worldView;
  private final GameUI gameUI;
  private final CameraController cameraController;
  private final InputMultiplexer inputMultiplexer;

  @Inject
  GameScreen(
      GL20 gl,
      Input input,
      WorldView worldView,
      GameUI gameUI,
      CameraController cameraController,
      WorldController worldController) {
    this.gl = gl;
    this.input = input;
    this.worldView = worldView;
    this.gameUI = gameUI;
    this.cameraController = cameraController;
    this.inputMultiplexer =
        new InputMultiplexer(gameUI.getInputProcessor(), cameraController, worldController);
  }

  @Override
  public void show() {
    cameraController.center();
    gameUI.init();
    input.setInputProcessor(inputMultiplexer);
  }

  @Override
  public void hide() {
    input.setInputProcessor(null);
  }

  @Override
  public void render(float delta) {
    gameUI.act(delta);
    gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    worldView.update(delta);
    gameUI.draw();
  }

  @Override
  public void resize(int width, int height) {
    // This is very important...
    worldView.resize(width, height);
    gameUI.resize(width, height);
  }

  @Override
  public void dispose() {
    gameUI.dispose();
  }
}