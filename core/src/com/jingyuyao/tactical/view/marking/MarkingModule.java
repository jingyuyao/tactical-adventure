package com.jingyuyao.tactical.view.marking;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.AssetModule;
import com.jingyuyao.tactical.model.map.MapObject;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.inject.Qualifier;
import javax.inject.Singleton;

public class MarkingModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(AssetManager.class);
  }

  /**
   * Uses a {@link LinkedHashMultimap} as the backing {@link MapObject} to {@link Sprite} map
   * to preserve the order of the {@link Sprite}. e.g. we always want select marker to be on top
   * of danger marker.
   */
  @Provides
  @BackingMap
  Multimap<MapObject, Sprite> provideBackingMap() {
    return LinkedHashMultimap.create();
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

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface BackingMap {

  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface Move {

  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface Hit {

  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface Attack {

  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface TargetSelect {

  }
}
