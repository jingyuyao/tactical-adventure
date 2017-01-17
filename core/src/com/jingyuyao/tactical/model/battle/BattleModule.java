package com.jingyuyao.tactical.model.battle;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Terrains;
import java.lang.annotation.Retention;

public class BattleModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(Characters.class);
    requireBinding(Terrains.class);

    bind(TargetFactory.class).annotatedWith(Piercing.class).to(PiercingTargetFactory.class);
    bind(TargetFactory.class).annotatedWith(Immediate.class).to(ImmediateTargetFactory.class);
  }


  @BindingAnnotation
  @java.lang.annotation.Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  public @interface Piercing {

  }

  @BindingAnnotation
  @java.lang.annotation.Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  public @interface Immediate {

  }
}
