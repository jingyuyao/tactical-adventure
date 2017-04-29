package com.jingyuyao.tactical.view.world.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.jingyuyao.tactical.view.world.WorldConfig;
import com.jingyuyao.tactical.view.world.component.CharacterComponent;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.LoopAnimation;
import com.jingyuyao.tactical.view.world.component.Moving;
import com.jingyuyao.tactical.view.world.component.PlayerComponent;
import com.jingyuyao.tactical.view.world.component.Position;
import com.jingyuyao.tactical.view.world.component.SingleAnimation;
import com.jingyuyao.tactical.view.world.resource.Animations;
import com.jingyuyao.tactical.view.world.resource.Markers;

public class SystemModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(Batch.class);
    requireBinding(WorldConfig.class);
    requireBinding(Engine.class);
    requireBinding(Animations.class);
    requireBinding(Markers.class);
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
