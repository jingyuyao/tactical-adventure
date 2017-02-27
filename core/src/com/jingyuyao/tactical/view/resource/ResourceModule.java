package com.jingyuyao.tactical.view.resource;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.google.inject.AbstractModule;

public class ResourceModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(TextureAtlas.class);

    bind(MarkerSprites.class);
  }
}
