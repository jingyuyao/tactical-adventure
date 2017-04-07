package com.jingyuyao.tactical.view;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.google.inject.AbstractModule;
import com.jingyuyao.tactical.controller.WorldCamera;
import com.jingyuyao.tactical.controller.WorldController;
import com.jingyuyao.tactical.view.ui.UIModule;
import com.jingyuyao.tactical.view.world.WorldModule;

public class ViewModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(GL20.class);
    requireBinding(Input.class);
    requireBinding(AssetManager.class);
    requireBinding(Skin.class);
    requireBinding(Batch.class);
    requireBinding(WorldController.class);
    requireBinding(WorldCamera.class);

    install(new UIModule());
    install(new WorldModule());
  }
}
