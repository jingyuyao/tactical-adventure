package com.jingyuyao.tactical.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.controller.MapController;
import com.jingyuyao.tactical.view.actor.MapActors;
import com.jingyuyao.tactical.view.marking.MapMarkings;
import com.jingyuyao.tactical.view.ui.MapUI;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MapScreen extends ScreenAdapter {

  private final MapActors mapActors;
  private final MapMarkings mapMarkings;
  private final MapUI mapUI;
  private final Batch batch;
  private final MapController mapController;

  @Inject
  MapScreen(
      MapActors mapActors,
      MapMarkings mapMarkings,
      MapUI mapUI,
      Batch batch,
      MapController mapController) {
    this.mapActors = mapActors;
    this.mapMarkings = mapMarkings;
    this.mapUI = mapUI;
    this.batch = batch;
    this.mapController = mapController;
  }

  public void registerListeners(EventBus eventBus) {
    eventBus.register(mapActors);
    eventBus.register(mapMarkings);
    eventBus.register(mapUI);
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
    mapUI.act(delta);
    mapMarkings.act(delta);
    mapActors.act(delta);

    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    mapActors.draw();
    mapMarkings.draw();
    mapUI.draw();
  }

  @Override
  public void resize(int width, int height) {
    // This is very important...
    mapActors.resize(width, height);
    mapUI.resize(width, height);
  }

  @Override
  public void dispose() {
    mapActors.dispose();
    mapUI.dispose();
    batch.dispose();
  }
}
