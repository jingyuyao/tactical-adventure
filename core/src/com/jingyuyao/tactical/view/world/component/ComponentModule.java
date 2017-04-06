package com.jingyuyao.tactical.view.world.component;

import com.badlogic.ashley.core.ComponentMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Key;

public class ComponentModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(new Key<ComponentMapper<Position>>() {
    }).toInstance(ComponentMapper.getFor(Position.class));
    bind(new Key<ComponentMapper<Frame>>() {
    }).toInstance(ComponentMapper.getFor(Frame.class));
    bind(new Key<ComponentMapper<LoopAnimation>>() {
    }).toInstance(ComponentMapper.getFor(LoopAnimation.class));
    bind(new Key<ComponentMapper<SingleAnimation>>() {
    }).toInstance(ComponentMapper.getFor(SingleAnimation.class));
    bind(new Key<ComponentMapper<Moving>>() {
    }).toInstance(ComponentMapper.getFor(Moving.class));
  }
}
