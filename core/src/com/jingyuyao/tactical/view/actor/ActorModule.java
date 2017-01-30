package com.jingyuyao.tactical.view.actor;

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
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.jingyuyao.tactical.AssetModule;
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
  @Singleton
  Map<String, Sprite> provideNameSpriteMap(AssetManager assetManager) {
    Map<String, Sprite> nameSpriteMap = new HashMap<String, Sprite>();
    nameSpriteMap.put("jingyu", new Sprite(assetManager.get(AssetModule.JINGYU, Texture.class)));
    nameSpriteMap.put("andrew", new Sprite(assetManager.get(AssetModule.ANDREW, Texture.class)));
    nameSpriteMap.put("kevin", new Sprite(assetManager.get(AssetModule.KEVIN, Texture.class)));
    nameSpriteMap.put("ben", new Sprite(assetManager.get(AssetModule.BEN, Texture.class)));
    nameSpriteMap.put("soldier", new Sprite(assetManager.get(AssetModule.SOLDIER, Texture.class)));
    return nameSpriteMap;
  }

  @Provides
  @ActorWorldSize
  float provideActorWorldSize() {
    return 1f;
  }

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  public @interface ActorWorldSize {

  }
}
