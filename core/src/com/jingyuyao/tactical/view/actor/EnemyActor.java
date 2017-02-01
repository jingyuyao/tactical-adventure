package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.google.common.collect.Multiset;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.view.actor.ActorModule.ActorWorldSize;
import javax.inject.Inject;

public class EnemyActor extends CharacterActor<Enemy> {

  @Inject
  EnemyActor(
      @Assisted Enemy object,
      @Assisted EventListener listener,
      @ActorWorldSize float size,
      Multiset<Sprite> markerSprites,
      @Assisted Sprite sprite) {
    super(object, listener, size, markerSprites, sprite);
    setColor(Color.RED);
  }
}
