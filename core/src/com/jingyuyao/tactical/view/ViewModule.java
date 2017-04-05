package com.jingyuyao.tactical.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.google.inject.AbstractModule;
import com.jingyuyao.tactical.controller.WorldController;
import com.jingyuyao.tactical.view.actor.ActorModule;
import com.jingyuyao.tactical.view.marking.MarkingModule;
import com.jingyuyao.tactical.view.resource.ResourceModule;
import com.jingyuyao.tactical.view.ui.UIModule;
import com.jingyuyao.tactical.view.world.WorldModule;

public class ViewModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(GL20.class);
    requireBinding(AssetManager.class);
    requireBinding(Skin.class);
    requireBinding(Batch.class);
    requireBinding(WorldController.class);

    install(new ResourceModule());
    install(new ActorModule());
    install(new MarkingModule());
    install(new UIModule());
    install(new WorldModule());
  }
}
