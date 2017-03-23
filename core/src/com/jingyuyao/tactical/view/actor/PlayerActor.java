package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.view.actor.ActorModule.InitialMarkers;
import com.jingyuyao.tactical.view.resource.LoopAnimation;
import com.jingyuyao.tactical.view.resource.WorldTexture;
import java.util.LinkedHashSet;
import javax.inject.Inject;

public class PlayerActor extends CharacterActor<Player> {

  @Inject
  PlayerActor(
      @Assisted Player object,
      @Assisted Coordinate initialCoordinate,
      ActorConfig actorConfig,
      @InitialMarkers LinkedHashSet<WorldTexture> markers,
      @Assisted LoopAnimation loopAnimation) {
    super(object, initialCoordinate, actorConfig, markers, loopAnimation);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    setColor(getObject().isActionable() ? Color.WHITE : Color.GRAY);
    super.draw(batch, parentAlpha);
  }
}
