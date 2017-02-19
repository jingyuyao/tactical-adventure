package com.jingyuyao.tactical.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.controller.MapController;
import com.jingyuyao.tactical.view.marking.WorldMarkings;
import com.jingyuyao.tactical.view.ui.WorldUI;
import com.jingyuyao.tactical.view.world.World;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MapScreen extends ScreenAdapter {

  private final World world;
  private final WorldMarkings worldMarkings;
  private final WorldUI worldUI;
  private final Batch batch;
  private final MapController mapController;

  @Inject
  MapScreen(
      World world,
      WorldMarkings worldMarkings,
      WorldUI worldUI,
      Batch batch,
      MapController mapController) {
    this.world = world;
    this.worldMarkings = worldMarkings;
    this.worldUI = worldUI;
    this.batch = batch;
    this.mapController = mapController;
  }

  public void registerListeners(EventBus eventBus) {
    eventBus.register(world);
    eventBus.register(worldMarkings);
    eventBus.register(worldUI);
  }

  @Override
  public void show() {
    mapController.receiveInput();
  }

  @Override
  public void hide() {
    mapController.stopReceivingInput();
  }

  @Override
  public void render(float delta) {
    worldUI.act(delta);
    worldMarkings.act(delta);
    world.act(delta);

    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    world.draw();
    worldMarkings.draw();
    worldUI.draw();
  }

  @Override
  public void resize(int width, int height) {
    // This is very important...
    world.resize(width, height);
    worldUI.resize(width, height);
  }

  @Override
  public void dispose() {
    world.dispose();
    worldUI.dispose();
    batch.dispose();
  }
}
