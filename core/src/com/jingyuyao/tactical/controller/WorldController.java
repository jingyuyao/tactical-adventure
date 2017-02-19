package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jingyuyao.tactical.view.ui.UIModule.WorldUIStage;
import com.jingyuyao.tactical.view.world.WorldModule.WorldStage;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldController {

  private final InputMultiplexer inputMultiplexer;

  @Inject
  WorldController(
      @WorldStage Stage mapViewStage,
      @WorldUIStage Stage mapUIStage,
      CameraController cameraController) {
    inputMultiplexer = new InputMultiplexer();
    inputMultiplexer.addProcessor(mapUIStage);
    inputMultiplexer.addProcessor(cameraController);
    inputMultiplexer.addProcessor(mapViewStage);
  }

  public void receiveInput() {
    Gdx.input.setInputProcessor(inputMultiplexer);
  }

  public void stopReceivingInput() {
    Gdx.input.setInputProcessor(null);
  }
}
