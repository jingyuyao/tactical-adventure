package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.view.actor.ActorModule.InitialMarkers;
import com.jingyuyao.tactical.view.resource.LoopAnimation;
import java.util.LinkedHashSet;
import javax.inject.Inject;

public class EnemyActor extends CharacterActor<Enemy> {

  @Inject
  EnemyActor(
      @Assisted Enemy object,
      ActorConfig actorConfig,
      @InitialMarkers LinkedHashSet<Sprite> markers,
      @Assisted LoopAnimation loopAnimation) {
    super(object, actorConfig, markers, loopAnimation);
    setColor(Color.RED);
  }
}
