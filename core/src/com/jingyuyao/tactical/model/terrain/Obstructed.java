package com.jingyuyao.tactical.model.terrain;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.i18n.Message;
import com.jingyuyao.tactical.model.i18n.ModelBundle;

public class Obstructed implements Terrain {

  @Override
  public Message getName() {
    return ModelBundle.TERRAIN_NAME.get("obstructed");
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
