package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jingyuyao.tactical.view.ui.UIModule.UIStage;
import com.jingyuyao.tactical.view.world.WorldModule.WorldStage;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldController {

  private final Input input;
  private final InputMultiplexer inputMultiplexer;

  @Inject
  WorldController(
      Input input,
      @WorldStage Stage mapViewStage,
      @UIStage Stage mapUIStage,
      WorldCamera worldCamera) {
    this.input = input;
    inputMultiplexer = new InputMultiplexer();
    inputMultiplexer.addProcessor(mapUIStage);
    inputMultiplexer.addProcessor(worldCamera);
    inputMultiplexer.addProcessor(mapViewStage);
  }

  public void receiveInput() {
    input.setInputProcessor(inputMultiplexer);
  }

  public void stopReceivingInput() {
    input.setInputProcessor(null);
  }
}
