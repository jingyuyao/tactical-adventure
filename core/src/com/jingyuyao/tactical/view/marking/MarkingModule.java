package com.jingyuyao.tactical.view.marking;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Provides;
import com.jingyuyao.tactical.AssetModule;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.inject.Singleton;

public class MarkingModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(AssetManager.class);
  }

  @Provides
  @Singleton
  @Move
  Sprite provideMoveSprite(AssetManager assetManager) {
    return new Sprite(assetManager.get(AssetModule.MOVE, Texture.class));
  }

  @Provides
  @Singleton
  @Hit
  Sprite provideHitSprite(AssetManager assetManager) {
    return new Sprite(assetManager.get(AssetModule.HIT, Texture.class));
  }

  @Provides
  @Singleton
  @TargetSelect
  Sprite provideTargetSelectSprite(AssetManager assetManager) {
    return new Sprite(assetManager.get(AssetModule.TARGET_SELECT, Texture.class));
  }

  @Provides
  @Singleton
  @Attack
  Sprite provideAttackSprite(AssetManager assetManager) {
    return new Sprite(assetManager.get(AssetModule.ATTACK, Texture.class));
  }

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface Move {

  }

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface Hit {

  }

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface Attack {

  }

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface TargetSelect {

  }
}
