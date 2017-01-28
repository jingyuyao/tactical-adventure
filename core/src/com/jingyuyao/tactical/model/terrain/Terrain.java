package com.jingyuyao.tactical.model.terrain;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.map.MapObject;

public interface Terrain extends MapObject {

  boolean canHold(Character character);

  int getMovementPenalty();
}
