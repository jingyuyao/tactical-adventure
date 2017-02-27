package com.jingyuyao.tactical.view.resource;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Qualifier;
import javax.inject.Singleton;

public class ResourceModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(TextureAtlas.class);

    install(new FactoryModuleBuilder().build(MyAnimationFactory.class));
  }

  @Provides
  @Singleton
  @BackingAnimationMap
  Map<String, MyAnimation> provideBackingAnimationMap() {
    return new HashMap<>();
  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface BackingAnimationMap {

  }
}
