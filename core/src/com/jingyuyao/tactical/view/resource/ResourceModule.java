package com.jingyuyao.tactical.view.resource;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.jingyuyao.tactical.view.actor.ActorConfig;
import com.jingyuyao.tactical.view.world.WorldConfig;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Qualifier;
import javax.inject.Singleton;

public class ResourceModule extends AbstractModule {

  public static final String TEXTURE_ATLAS = "packed/texture.atlas";
  public static final String SKIN = "ui/uiskin.json";

  @Override
  protected void configure() {
    requireBinding(AssetManager.class);
    requireBinding(ActorConfig.class);
    requireBinding(WorldConfig.class);

    install(new FactoryModuleBuilder().build(AnimationFactory.class));
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
  @WorldTextureCache
  Map<String, WorldTexture[]> provideWorldTextureCache() {
    return new HashMap<>();
  }

  @Provides
  @Singleton
  @AnimationBus
  EventBus provideAnimationBus() {
    return new EventBus();
  }

  @Provides
  @Singleton
  TextureAtlas provideTextureAtlas(AssetManager assetManager) {
    assetManager.load(TEXTURE_ATLAS, TextureAtlas.class);
    assetManager.finishLoading();
    return assetManager.get(TEXTURE_ATLAS, TextureAtlas.class);
  }

  @Provides
  @Singleton
  Skin provideSkin(AssetManager assetManager) {
    assetManager.load(SKIN, Skin.class);
    assetManager.finishLoading();
    return assetManager.get(SKIN, Skin.class);
  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface LoopAnimationCache {

  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface WorldTextureCache {

  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface AnimationBus {

  }
}
