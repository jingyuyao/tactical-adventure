package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.view.actor.ActorModule.InitialMarkers;
import com.jingyuyao.tactical.view.resource.LoopAnimation;
import java.util.LinkedHashSet;
import javax.inject.Inject;

public class PlayerActor extends CharacterActor<Player> {

  @Inject
  PlayerActor(
      @Assisted Player object,
      ActorConfig actorConfig,
      @InitialMarkers LinkedHashSet<Sprite> markers,
      @Assisted LoopAnimation loopAnimation) {
    super(object, actorConfig, markers, loopAnimation);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    setColor(getObject().isActionable() ? Color.WHITE : Color.GRAY);
    super.draw(batch, parentAlpha);
  }
}
