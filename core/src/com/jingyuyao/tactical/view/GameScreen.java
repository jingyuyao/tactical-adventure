package com.jingyuyao.tactical.view;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.jingyuyao.tactical.GameState;
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
  private final GameState gameState;
  private final WorldView worldView;
  private final GameUI gameUI;
  private final InputMultiplexer inputMultiplexer;

  @Inject
  GameScreen(
      GL20 gl,
      Input input,
      GameState gameState,
      WorldView worldView,
      GameUI gameUI,
      CameraController cameraController,
      WorldController worldController) {
    this.gl = gl;
    this.input = input;
    this.gameState = gameState;
    this.worldView = worldView;
    this.gameUI = gameUI;
    this.inputMultiplexer =
        new InputMultiplexer(
            this.new BackKeyProcessor(),
            gameUI.getInputProcessor(),
            cameraController,
            worldController);
  }

  @Override
  public void show() {
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

  class BackKeyProcessor extends InputAdapter {

    @Override
    public boolean keyDown(int keycode) {
      if (keycode == Keys.BACK) {
        gameState.goToStartMenu();
        return true;
      }
      return false;
    }
  }
}
