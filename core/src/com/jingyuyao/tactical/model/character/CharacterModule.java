package com.jingyuyao.tactical.model.character;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import com.jingyuyao.tactical.model.retaliation.PassiveRetaliation;
import com.jingyuyao.tactical.model.retaliation.Retaliation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

public class CharacterModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new FactoryModuleBuilder()
        .implement(Enemy.class, Names.named("PassiveEnemy"), PassiveEnemy.class)
        .build(CharacterFactory.class));

    bind(Retaliation.class)
        .annotatedWith(DefaultRetaliation.class)
        .to(PassiveRetaliation.class);
  }

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface DefaultRetaliation {

  }
}
