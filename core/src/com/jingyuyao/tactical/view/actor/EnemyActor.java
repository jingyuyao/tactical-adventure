package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.Color;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.view.resource.LoopAnimation;
import com.jingyuyao.tactical.view.resource.WorldTexture;
import java.util.LinkedHashSet;

public class EnemyActor extends CharacterActor<Enemy> {

  EnemyActor(
      Enemy object,
      Coordinate initialCoordinate,
      ActorConfig actorConfig,
      LinkedHashSet<WorldTexture> markers,
      LoopAnimation loopAnimation) {
    super(object, initialCoordinate, actorConfig, markers, loopAnimation);
    setColor(Color.RED);
  }
}
