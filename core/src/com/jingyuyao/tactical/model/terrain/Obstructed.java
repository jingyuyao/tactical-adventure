package com.jingyuyao.tactical.model.terrain;

import com.jingyuyao.tactical.model.character.Character;

public class Obstructed implements Terrain {

  @Override
  public boolean canHold(Character character) {
    return true;
  }

  @Override
  public int getMovementPenalty() {
    return 2;
  }
}
