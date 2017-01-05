package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.AssetModule;
import com.jingyuyao.tactical.model.mark.Marker;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

public class ActorModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(ActorFactory.class);
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
        Marker.DANGER, new Sprite(assetManager.get(AssetModule.DANGER, Texture.class)));
    markerSpriteMap.put(
        Marker.CAN_ATTACK, new Sprite(assetManager.get(AssetModule.ATTACK, Texture.class)));
    markerSpriteMap.put(
        Marker.POTENTIAL_TARGET,
        new Sprite(assetManager.get(AssetModule.POTENTIAL_TARGET, Texture.class)));
    markerSpriteMap.put(
        Marker.CHOSEN_TARGET,
        new Sprite(assetManager.get(AssetModule.CHOSEN_TARGET, Texture.class)));
    return markerSpriteMap;
  }

  // TODO: find a better way
  @Provides
  @Singleton
  Map<String, Sprite> provideCharacterSpriteMap(AssetManager assetManager) {
    Map<String, Sprite> characterSpriteMap = new HashMap<String, Sprite>();
    characterSpriteMap.put("billy", new Sprite(assetManager.get(AssetModule.BILLY, Texture.class)));
    characterSpriteMap.put("john", new Sprite(assetManager.get(AssetModule.JOHN, Texture.class)));
    return characterSpriteMap;
  }
}
