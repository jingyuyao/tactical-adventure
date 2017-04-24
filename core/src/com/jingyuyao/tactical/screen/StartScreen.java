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

  private final Stage stage;
  private final GL20 gl;
  private final Input input;
  private final StartScreenLayout startScreenLayout;

  @Inject
  StartScreen(
      @MenuScreenStage Stage stage, GL20 gl, Input input, StartScreenLayout startScreenLayout) {
    this.stage = stage;
    this.gl = gl;
    this.input = input;
    this.startScreenLayout = startScreenLayout;
    stage.addActor(startScreenLayout.getTable());
  }

  @Override
  public void show() {
    input.setInputProcessor(stage);
    startScreenLayout.show();
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
