package com.jingyuyao.tactical.model.terrain;

import com.jingyuyao.tactical.model.character.Character;

public interface Terrain {

  boolean canHold(Character character);

  int getMovementPenalty();
}
