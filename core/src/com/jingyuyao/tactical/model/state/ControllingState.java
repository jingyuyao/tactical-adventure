package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.world.Cell;

public interface ControllingState extends State {

  /**
   * The {@link Cell} being controlled.
   */
  Cell getCell();

  /**
   * The {@link Ship} being controlled.
   */
  Ship getShip();
}
