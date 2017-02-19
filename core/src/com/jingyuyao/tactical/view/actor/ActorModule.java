package com.jingyuyao.tactical.view.actor;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.LinkedHashSet;
import javax.inject.Qualifier;

public class ActorModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new FactoryModuleBuilder().build(ActorFactory.class));
  }

  @Provides
  @InitialMarkers
  LinkedHashSet<Sprite> provideInitialMarkers() {
    return new LinkedHashSet<>();
  }

  @Provides
  @ActorSize
  float provideActorSize(ActorConfig actorConfig) {
    return actorConfig.getActorSize();
  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface ActorSize {

  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface InitialMarkers {

  }
}
