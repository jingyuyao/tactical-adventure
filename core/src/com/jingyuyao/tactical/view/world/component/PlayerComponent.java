package com.jingyuyao.tactical.view.world.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.jingyuyao.tactical.model.ship.Player;

public class PlayerComponent implements Component, Poolable {

  private Player player;

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  @Override
  public void reset() {
    player = null;
  }
}
