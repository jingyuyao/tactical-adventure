package com.jingyuyao.tactical.view.marking;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.AssetModule;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.view.MapActors;
import com.jingyuyao.tactical.view.actor.MapActor;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Qualifier;
import javax.inject.Singleton;

public class MarkingModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(AssetManager.class);
    requireBinding(Batch.class);
    requireBinding(MapActors.class);
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
  @HighlightSprite
  Sprite provideHighlightSprite(AssetManager assetManager) {
    return new Sprite(assetManager.get(AssetModule.HIGHLIGHT, Texture.class));
  }

  @Provides
  @Singleton
  @ActivatedCharacterSprite
  Sprite provideActivatedCharacterSprite(AssetManager assetManager) {
    return new Sprite(assetManager.get(AssetModule.ACTIVATED, Texture.class));
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

  @Provides
  @Singleton
  List<MapActor<?>> provideMarkedActorList() {
    return new LinkedList<>();
  }

  @Provides
  @Singleton
  @MapMarkingsActionActor
  Actor provideMapMarkingsActionActor() {
    return new Actor();
  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface HighlightSprite {

  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface ActivatedCharacterSprite {

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

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface MapMarkingsActionActor {

  }
}
