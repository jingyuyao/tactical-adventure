package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.world.Cell;

public interface PlayerState extends State {

  Cell getPlayerCell();

  Player getPlayer();
}
