package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.Color;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.view.actor.ActorModule.InitialMarkers;
import com.jingyuyao.tactical.view.resource.LoopAnimation;
import com.jingyuyao.tactical.view.resource.WorldTexture;
import java.util.LinkedHashSet;
import javax.inject.Inject;

public class EnemyActor extends CharacterActor<Enemy> {

  @Inject
  EnemyActor(
      @Assisted Enemy object,
      @Assisted Coordinate initialCoordinate,
      ActorConfig actorConfig,
      @InitialMarkers LinkedHashSet<WorldTexture> markers,
      @Assisted LoopAnimation loopAnimation) {
    super(object, initialCoordinate, actorConfig, markers, loopAnimation);
    setColor(Color.RED);
  }
}
