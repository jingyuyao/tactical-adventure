package com.jingyuyao.tactical.view.world.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.jingyuyao.tactical.view.world.component.CharacterComponent;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.LoopAnimation;
import com.jingyuyao.tactical.view.world.component.Moving;
import com.jingyuyao.tactical.view.world.component.PlayerComponent;
import com.jingyuyao.tactical.view.world.component.Position;
import com.jingyuyao.tactical.view.world.component.SingleAnimation;

public class SystemModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(Engine.class);
    requireBinding(new Key<ComponentMapper<Position>>() {
    });
    requireBinding(new Key<ComponentMapper<Frame>>() {
    });
    requireBinding(new Key<ComponentMapper<LoopAnimation>>() {
    });
    requireBinding(new Key<ComponentMapper<SingleAnimation>>() {
    });
    requireBinding(new Key<ComponentMapper<Moving>>() {
    });
    requireBinding(new Key<ComponentMapper<CharacterComponent>>() {
    });
    requireBinding(new Key<ComponentMapper<PlayerComponent>>() {
    });
  }
}
