package com.jingyuyao.tactical.view.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Enemy;
import java.util.LinkedHashSet;
import javax.inject.Inject;

class EnemyActor extends CharacterActor<Enemy> {

  @Inject
  EnemyActor(
      @Assisted Enemy object,
      @ActorModule.ActorWorldSize float size,
      @ActorModule.InitialMarkers LinkedHashSet<Sprite> markers,
      @Assisted Sprite sprite) {
    super(object, size, markers, sprite);
    setColor(Color.RED);
  }
}
