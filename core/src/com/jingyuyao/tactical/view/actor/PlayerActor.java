package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.view.resource.LoopAnimation;
import com.jingyuyao.tactical.view.resource.WorldTexture;
import java.util.LinkedHashSet;

public class PlayerActor extends CharacterActor {

  private final Player player;

  PlayerActor(
      Player player,
      float moveTimePerUnit,
      LinkedHashSet<WorldTexture> markers,
      LoopAnimation loopAnimation) {
    super(moveTimePerUnit, markers, loopAnimation);
    this.player = player;
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    setColor(player.isActionable() ? Color.WHITE : Color.GRAY);
    super.draw(batch, parentAlpha);
  }
}
