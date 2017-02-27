package com.jingyuyao.tactical.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.controller.ControllerFactory;
import com.jingyuyao.tactical.controller.WorldController;
import com.jingyuyao.tactical.view.actor.ActorModule;
import com.jingyuyao.tactical.view.marking.MarkingModule;
import com.jingyuyao.tactical.view.resource.ResourceModule;
import com.jingyuyao.tactical.view.ui.UIModule;
import com.jingyuyao.tactical.view.world.WorldModule;
import javax.inject.Singleton;

public class ViewModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(AssetManager.class);
    requireBinding(TextureAtlas.class);
    requireBinding(WorldController.class);
    requireBinding(ControllerFactory.class);

    install(new ResourceModule());
    install(new ActorModule());
    install(new MarkingModule());
    install(new UIModule());
    install(new WorldModule());
  }

  @Provides
  @Singleton
  Batch provideBatch() {
    return new SpriteBatch();
  }
}
