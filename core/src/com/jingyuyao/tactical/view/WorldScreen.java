package com.jingyuyao.tactical.view;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.view.ui.UI;
import com.jingyuyao.tactical.view.world.WorldView;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldScreen extends ScreenAdapter {

  private final GL20 gl;
  private final Input input;
  private final WorldView worldView;
  private final UI ui;

  @Inject
  WorldScreen(GL20 gl, Input input, WorldView worldView, UI ui) {
    this.gl = gl;
    this.input = input;
    this.worldView = worldView;
    this.ui = ui;
  }

  public void register(EventBus eventBus) {
    worldView.register(eventBus);
    ui.register(eventBus);
  }

  @Override
  public void show() {
    input.setInputProcessor(
        new InputMultiplexer(ui.getInputProcessor(), worldView.getInputProcessor()));
  }

  @Override
  public void hide() {
    input.setInputProcessor(null);
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
