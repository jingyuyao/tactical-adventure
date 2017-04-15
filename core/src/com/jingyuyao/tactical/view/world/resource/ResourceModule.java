package com.jingyuyao.tactical.view.world.resource;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.jingyuyao.tactical.view.world.WorldConfig;
import com.jingyuyao.tactical.view.world.component.LoopAnimation;
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
    requireBinding(WorldConfig.class);

    install(new FactoryModuleBuilder().build(TextureFactory.class));
  }

  @Provides
  @Singleton
  @LoopAnimationCache
  Map<String, LoopAnimation> provideLoopAnimationCache() {
    return new HashMap<>();
  }

  @Provides
  @Singleton
  @AtlasRegionsCache
  Map<String, WorldTexture[]> provideAtlasRegionsCache() {
    return new HashMap<>();
  }

  @Provides
  @Singleton
  @MarkerTextureCache
  Map<String, WorldTexture> provideMarkerTextureCache() {
    return new HashMap<>();
  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface LoopAnimationCache {

  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface AtlasRegionsCache {

  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface MarkerTextureCache {

  }
}
