package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.view.resource.LoopAnimation;
import com.jingyuyao.tactical.view.resource.WorldTexture;
import java.util.LinkedHashSet;

public class PlayerActor extends CharacterActor<Player> {

  PlayerActor(
      Player object,
      Coordinate initialCoordinate,
      ActorConfig actorConfig,
      LinkedHashSet<WorldTexture> markers,
      LoopAnimation loopAnimation) {
    super(object, initialCoordinate, actorConfig, markers, loopAnimation);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    setColor(getObject().isActionable() ? Color.WHITE : Color.GRAY);
    super.draw(batch, parentAlpha);
  }
}
