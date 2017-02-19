package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.view.marking.MarkingModule;
import com.jingyuyao.tactical.view.ui.UIModule;
import com.jingyuyao.tactical.view.world.ActorModule;
import javax.inject.Singleton;

public class ViewModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new ActorModule());
    install(new MarkingModule());
    install(new UIModule());
  }

  @Provides
  @Singleton
  Batch provideBatch() {
    return new SpriteBatch();
  }
}
