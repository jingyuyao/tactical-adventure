package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.world.Cell;

public interface PlayerState extends State {

  Cell getPlayerCell();

  Ship getPlayer();
}
