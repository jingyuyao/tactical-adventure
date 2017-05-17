package com.jingyuyao.tactical.view.world.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.jingyuyao.tactical.model.ship.Ship;

public class PlayerComponent implements Component, Poolable {

  private Ship player;

  public Ship getPlayer() {
    return player;
  }

  public void setPlayer(Ship player) {
    this.player = player;
  }

  @Override
  public void reset() {
    player = null;
  }
}
