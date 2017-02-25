package com.jingyuyao.tactical.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.jingyuyao.tactical.controller.WorldController;
import com.jingyuyao.tactical.view.marking.Markings;
import com.jingyuyao.tactical.view.ui.UI;
import com.jingyuyao.tactical.view.world.World;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldScreen extends ScreenAdapter {

  private final Batch batch;
  private final World world;
  private final Markings markings;
  private final UI ui;
  private final WorldController worldController;

  @Inject
  WorldScreen(
      Batch batch,
      World world,
      Markings markings,
      UI ui,
      WorldController worldController) {
    this.batch = batch;
    this.world = world;
    this.markings = markings;
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
    markings.act(delta);
    world.act(delta);

    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    world.draw();
    markings.draw();
    ui.draw();
  }

  @Override
  public void resize(int width, int height) {
    // This is very important...
    world.resize(width, height);
    ui.resize(width, height);
  }

  @Override
  public void dispose() {
    world.dispose();
    ui.dispose();
    batch.dispose();
  }
}
