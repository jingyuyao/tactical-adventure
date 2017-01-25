package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.controller.InputLock;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.view.actor.ActorAnnotations.ActorWorldSize;
import com.jingyuyao.tactical.view.actor.ActorAnnotations.EnemySprite;
import java.util.Map;
import javax.inject.Inject;

public class EnemyActor extends CharacterActor<Enemy> {

  @Inject
  EnemyActor(
      @Assisted Enemy object,
      @Assisted EventListener listener,
      @ActorWorldSize float size,
      Map<Marker, Sprite> markerSpriteMap,
      @EnemySprite Sprite sprite,
      InputLock inputLock) {
    super(object, listener, size, markerSpriteMap, sprite, inputLock);
    setColor(Color.RED);
  }
}