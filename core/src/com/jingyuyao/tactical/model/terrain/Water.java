package com.jingyuyao.tactical.model.terrain;

import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.map.Coordinate;
import javax.inject.Inject;

public class Water extends AbstractTerrain {

  @Inject
  Water(@Assisted Coordinate coordinate) {
    super(coordinate);
  }

  @Override
  public boolean canHold(Character character) {
    return false;
  }

  @Override
  public int getMovementPenalty() {
    return 0;
  }
}
