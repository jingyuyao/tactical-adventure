package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jingyuyao.tactical.view.ViewModule.MapUiStage;
import com.jingyuyao.tactical.view.ViewModule.MapViewStage;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MapController {

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

  public void receiveInput() {
    Gdx.input.setInputProcessor(inputMultiplexer);
  }
}
