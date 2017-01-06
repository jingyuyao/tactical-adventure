package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.jingyuyao.tactical.AssetModule;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.view.actor.ActorConfig.ActorWorldSize;
import com.jingyuyao.tactical.view.actor.ActorConfig.EnemySprite;
import com.jingyuyao.tactical.view.actor.ActorConfig.InitialEnemyTint;
import com.jingyuyao.tactical.view.actor.ActorConfig.InitialMarkerSprites;
import com.jingyuyao.tactical.view.actor.ActorConfig.InitialPlayerTint;
import com.jingyuyao.tactical.view.actor.ActorConfig.PlayerSprite;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Singleton;

public class ActorModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new FactoryModuleBuilder().build(ActorFactory.class));

    bind(ActorConfig.class);
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

  @Provides
  @ActorWorldSize
  float provideActorWorldSize(ActorConfig actorConfig) {
    return actorConfig.getActorWorldSize();
  }

  @Provides
  @InitialPlayerTint
  Color provideInitialPlayerColor(ActorConfig actorConfig) {
    return actorConfig.getInitialPlayerTint();
  }

  @Provides
  @InitialEnemyTint
  Color provideInitialEnemyColor(ActorConfig actorConfig) {
    return actorConfig.getInitialEnemyTint();
  }

  @Provides
  @InitialMarkerSprites
  List<Sprite> provideInitialMarkerSprites() {
    return new ArrayList<Sprite>();
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
}
