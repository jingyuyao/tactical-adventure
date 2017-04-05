package com.jingyuyao.tactical.view.actor;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.view.resource.Animations;
import com.jingyuyao.tactical.view.resource.WorldTexture;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.LinkedHashSet;
import javax.inject.Qualifier;

public class ActorModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(Animations.class);
  }

  @Provides
  @InitialMarkers
  LinkedHashSet<WorldTexture> provideInitialMarkers() {
    return new LinkedHashSet<>();
  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface InitialMarkers {

  }
}
