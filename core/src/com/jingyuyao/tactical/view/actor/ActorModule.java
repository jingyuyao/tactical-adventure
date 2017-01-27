package com.jingyuyao.tactical.view.actor;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.jingyuyao.tactical.AssetModule;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Marker;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Singleton;

public class ActorModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new FactoryModuleBuilder().build(ActorFactory.class));
  }

  @Provides
  @Singleton
  Map<Marker, Sprite> provideMarkerSpriteMap(AssetManager assetManager) {
    Map<Marker, Sprite> markerSpriteMap = new HashMap<Marker, Sprite>();
    markerSpriteMap.put(
        Marker.HIGHLIGHT, new Sprite(assetManager.get(AssetModule.HIGHLIGHT, Texture.class)));
    markerSpriteMap.put(
        Marker.CAN_MOVE_TO, new Sprite(assetManager.get(AssetModule.MOVE, Texture.class)));
    markerSpriteMap.put(
        Marker.HIT, new Sprite(assetManager.get(AssetModule.HIT, Texture.class)));
    markerSpriteMap.put(
        Marker.CAN_ATTACK, new Sprite(assetManager.get(AssetModule.ATTACK, Texture.class)));
    markerSpriteMap.put(
        Marker.POTENTIAL_TARGET,
        new Sprite(assetManager.get(AssetModule.POTENTIAL_TARGET, Texture.class)));
    markerSpriteMap.put(
        Marker.TARGET_SELECT,
        new Sprite(assetManager.get(AssetModule.TARGET_SELECT, Texture.class)));
    return markerSpriteMap;
  }

  @Provides
  @ActorWorldSize
  float provideActorWorldSize() {
    return 1f;
  }

  @Provides
  @InitialMarkerSprites
  Multiset<Sprite> provideInitialMarkerSprites() {
    return HashMultiset.create();
  }

  // TODO: temp, remove me
  @Provides
  @Singleton
  @PlayerSprite
  Sprite providePlayerSprite(AssetManager assetManager) {
    return new Sprite(assetManager.get(AssetModule.JOHN, Texture.class));
  }

  // TODO: temp, remove me
  @Provides
  @Singleton
  @EnemySprite
  Sprite provideEnemySprite(AssetManager assetManager) {
    return new Sprite(assetManager.get(AssetModule.BILLY, Texture.class));
  }

  @Provides
  @Singleton
  @BackingActorMap
  Map<MapObject, MapActor> provideBackingActorMap() {
    return new HashMap<MapObject, MapActor>();
  }

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  public @interface ActorWorldSize {

  }

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface InitialMarkerSprites {

  }

  // TODO: temp, remove me later
  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface PlayerSprite {

  }

  // TODO: temp, remove me later
  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface EnemySprite {

  }

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface BackingActorMap {

  }
}
