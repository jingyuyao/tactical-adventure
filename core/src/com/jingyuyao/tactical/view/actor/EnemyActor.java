package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.view.actor.ActorModule.ActorWorldSize;
import com.jingyuyao.tactical.view.actor.ActorModule.InitialMarkers;
import java.util.LinkedHashSet;
import javax.inject.Inject;

public class EnemyActor extends CharacterActor<Enemy> {

  @Inject
  EnemyActor(
      @Assisted Enemy object,
      @ActorWorldSize float size,
      @InitialMarkers LinkedHashSet<Sprite> markers,
      @Assisted Sprite sprite) {
    super(object, size, markers, sprite);
    setColor(Color.RED);
  }
}
