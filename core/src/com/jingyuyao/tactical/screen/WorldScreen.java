package com.jingyuyao.tactical.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.view.ui.WorldUI;
import com.jingyuyao.tactical.view.world.WorldView;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldScreen extends ScreenAdapter {

  private final GL20 gl;
  private final Input input;
  private final WorldView worldView;
  private final WorldUI worldUI;

  @Inject
  WorldScreen(GL20 gl, Input input, WorldView worldView, WorldUI worldUI) {
    this.gl = gl;
    this.input = input;
    this.worldView = worldView;
    this.worldUI = worldUI;
  }

  public void register(EventBus eventBus) {
    worldView.register(eventBus);
    worldUI.register(eventBus);
  }

  @Override
  public void show() {
    worldView.center();
    input.setInputProcessor(
        new InputMultiplexer(worldUI.getInputProcessor(), worldView.getInputProcessor()));
  }

  @Override
  public void hide() {
    input.setInputProcessor(null);
  }

  @Override
  public void render(float delta) {
    worldUI.act(delta);
    gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    worldView.update(delta);
    worldUI.draw();
  }

  @Override
  public void resize(int width, int height) {
    // This is very important...
    worldView.resize(width, height);
    worldUI.resize(width, height);
  }

  @Override
  public void dispose() {
    worldUI.dispose();
  }
}
