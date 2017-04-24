package com.jingyuyao.tactical.model.terrain;

import com.jingyuyao.tactical.model.character.Character;

public class Land implements Terrain {

  @Override
  public String getName() {
    return "Land";
  }

  @Override
  public boolean canHold(Character character) {
    return true;
  }

  @Override
  public int getMovementPenalty() {
    return 1;
  }
}
