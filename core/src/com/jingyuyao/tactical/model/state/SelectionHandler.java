package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.terrain.Terrain;

public interface SelectionHandler {

  void select(Player player);

  void select(Enemy enemy);

  void select(Terrain terrain);
}
