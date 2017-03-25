package com.jingyuyao.tactical.model.terrain;

import com.jingyuyao.tactical.model.character.Character;

public class Water implements Terrain {

  @Override
  public boolean canHold(Character character) {
    return false;
  }

  @Override
  public int getMovementPenalty() {
    return 0;
  }
}
