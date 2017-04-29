package com.jingyuyao.tactical.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.google.inject.AbstractModule;
import com.jingyuyao.tactical.controller.CameraController;
import com.jingyuyao.tactical.controller.WorldController;
import com.jingyuyao.tactical.view.ui.WorldUIModule;
import com.jingyuyao.tactical.view.world.WorldViewModule;

public class ViewModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(AssetManager.class);
    requireBinding(Batch.class);
    requireBinding(WorldController.class);
    requireBinding(CameraController.class);

    install(new WorldUIModule());
    install(new WorldViewModule());
  }
}
