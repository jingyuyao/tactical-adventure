package com.jingyuyao.tactical.model.terrain;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.i18n.Message;

public interface Terrain {

  Message getName();

  boolean canHold(Character character);

  int getMovementPenalty();
}
