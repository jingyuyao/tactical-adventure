package com.jingyuyao.tactical.view.marking;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.view.actor.WorldActor;
import com.jingyuyao.tactical.view.resource.Animations;
import com.jingyuyao.tactical.view.resource.Markers;
import com.jingyuyao.tactical.view.resource.SingleAnimation;
import com.jingyuyao.tactical.view.world.World;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Qualifier;
import javax.inject.Singleton;

public class MarkingModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(Animations.class);
    requireBinding(Markers.class);
    requireBinding(Batch.class);
    requireBinding(World.class);
  }

  @Provides
  @Singleton
  @MarkedActors
  List<WorldActor<?>> provideMarkedActors() {
    return new LinkedList<>();
  }

  @Provides
  @Singleton
  @InProgressAnimationsMap
  Multimap<WorldActor<?>, SingleAnimation> provideInProgressAnimationsMap() {
    return HashMultimap.create();
  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface MarkedActors {

  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface InProgressAnimationsMap {

  }
}
