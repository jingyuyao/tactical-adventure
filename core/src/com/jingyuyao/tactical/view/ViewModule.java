package com.jingyuyao.tactical.view;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.google.inject.AbstractModule;
import com.jingyuyao.tactical.controller.CameraController;
import com.jingyuyao.tactical.controller.WorldController;
import com.jingyuyao.tactical.data.MessageLoader;
import com.jingyuyao.tactical.view.ui.GameUIModule;
import com.jingyuyao.tactical.view.world.WorldViewModule;

public class ViewModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(GL20.class);
    requireBinding(Input.class);
    requireBinding(Batch.class);
    requireBinding(MessageLoader.class);
    requireBinding(WorldController.class);
    requireBinding(CameraController.class);

    install(new GameUIModule());
    install(new WorldViewModule());
  }
}
