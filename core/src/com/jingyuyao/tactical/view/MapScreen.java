package com.jingyuyao.tactical.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.google.common.eventbus.EventBus;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MapScreen extends ScreenAdapter {

  private final MapView mapView;
  private final MapMarkings mapMarkings;
  private final MapUI mapUI;
  private final Batch batch;

  @Inject
  MapScreen(MapView mapView, MapMarkings mapMarkings, MapUI mapUI, Batch batch) {
    this.mapView = mapView;
    this.mapMarkings = mapMarkings;
    this.mapUI = mapUI;
    this.batch = batch;
  }

  public void registerTo(EventBus eventBus) {
    eventBus.register(mapView);
    eventBus.register(mapMarkings);
    eventBus.register(mapUI);
  }

  @Override
  public void render(float delta) {
    mapUI.act(delta);
    mapMarkings.act(delta);
    mapView.act(delta);

    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    mapView.draw();
    mapMarkings.draw();
    mapUI.draw();
  }

  @Override
  public void resize(int width, int height) {
    // This is very important...
    mapView.resize(width, height);
    mapUI.resize(width, height);
  }

  @Override
  public void dispose() {
    mapView.dispose();
    mapUI.dispose();
    batch.dispose();
  }
}
