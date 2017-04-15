package com.jingyuyao.tactical.view.world.system;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.Singleton;
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
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;
import javax.inject.Qualifier;

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

  @Provides
  @Singleton
  @EntitySystems
  List<EntitySystem> provideEntitySystems(
      AnimationSystem animationSystem,
      CharacterSystem characterSystem,
      EffectsSystem effectsSystem,
      MarkerSystem markerSystem,
      MovingSystem movingSystem,
      PlayerSystem playerSystem,
      RemoveSystem removeSystem,
      RenderSystem renderSystem
  ) {
    return ImmutableList.of(
        animationSystem,
        characterSystem,
        effectsSystem,
        markerSystem,
        movingSystem,
        playerSystem,
        removeSystem,
        renderSystem
    );
  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  public @interface EntitySystems {

  }
}
