package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.common.EventSubscriber;
import com.jingyuyao.tactical.model.event.ClearMap;
import com.jingyuyao.tactical.model.event.NewMap;
import com.jingyuyao.tactical.view.ViewAnnotations.MapUiStage;
import com.jingyuyao.tactical.view.ViewAnnotations.MapViewStage;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MapController implements EventSubscriber {

  private final InputMultiplexer inputMultiplexer;

  @Inject
  MapController(
      @MapViewStage Stage mapViewStage,
      @MapUiStage Stage mapUIStage,
      DragCameraController dragCameraController) {
    inputMultiplexer = new InputMultiplexer();
    inputMultiplexer.addProcessor(mapUIStage);
    inputMultiplexer.addProcessor(dragCameraController);
    inputMultiplexer.addProcessor(mapViewStage);
  }

  @Subscribe
  public void initialize(NewMap data) {
    Gdx.input.setInputProcessor(inputMultiplexer);
  }

  @Subscribe
  public void dispose(ClearMap data) {
    // Do we need to remove input processor?
  }
}
