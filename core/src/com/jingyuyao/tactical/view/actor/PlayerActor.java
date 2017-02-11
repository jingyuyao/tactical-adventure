package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.view.actor.ActorModule.ActorWorldSize;
import com.jingyuyao.tactical.view.actor.ActorModule.InitialMarkers;
import java.util.LinkedHashSet;
import javax.inject.Inject;

public class PlayerActor extends CharacterActor<Player> {

  @Inject
  PlayerActor(
      @Assisted Player object,
      @Assisted EventListener listener,
      @ActorWorldSize float size,
      @InitialMarkers LinkedHashSet<Sprite> markers,
      @Assisted Sprite sprite) {
    super(object, listener, size, markers, sprite);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    setColor(getObject().isActionable() ? Color.WHITE : Color.GRAY);
    super.draw(batch, parentAlpha);
  }
}
