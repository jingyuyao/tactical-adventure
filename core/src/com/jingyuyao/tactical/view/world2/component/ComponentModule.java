package com.jingyuyao.tactical.view.world2.component;

import com.badlogic.ashley.core.ComponentMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.jingyuyao.tactical.view.resource.LoopAnimation;
import com.jingyuyao.tactical.view.resource.WorldTexture;

public class ComponentModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(new Key<ComponentMapper<Position>>() {
    }).toInstance(ComponentMapper.getFor(Position.class));
    bind(new Key<ComponentMapper<WorldTexture>>() {
    }).toInstance(ComponentMapper.getFor(WorldTexture.class));
    bind(new Key<ComponentMapper<LoopAnimation>>() {
    }).toInstance(ComponentMapper.getFor(LoopAnimation.class));
  }
}
