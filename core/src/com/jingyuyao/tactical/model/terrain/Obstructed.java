package com.jingyuyao.tactical.model.terrain;

import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.map.Coordinate;
import javax.inject.Inject;

public class Obstructed extends AbstractTerrain {

  @Inject
  Obstructed(@Assisted Coordinate coordinate) {
    super(coordinate);
  }

  @Override
  public boolean canHold(Character character) {
    return true;
  }

  @Override
  public int getMovementPenalty() {
    return 2;
  }
}
