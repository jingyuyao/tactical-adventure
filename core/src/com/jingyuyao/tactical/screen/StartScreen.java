package com.jingyuyao.tactical.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jingyuyao.tactical.screen.ScreenModule.MenuScreenStage;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class StartScreen extends ScreenAdapter {

  private final GL20 gl;
  private final Input input;
  private final Stage stage;

  @Inject
  StartScreen(
      GL20 gl, Input input, @MenuScreenStage Stage stage, StartScreenLayout startScreenLayout) {
    this.gl = gl;
    this.input = input;
    this.stage = stage;
    this.stage.addActor(startScreenLayout);
  }

  @Override
  public void show() {
    input.setInputProcessor(stage);
  }

  @Override
  public void hide() {
    input.setInputProcessor(null);
  }

  @Override
  public void render(float delta) {
    stage.act(delta);
    gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    stage.getViewport().apply();
    stage.draw();
  }

  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height);
  }
}
