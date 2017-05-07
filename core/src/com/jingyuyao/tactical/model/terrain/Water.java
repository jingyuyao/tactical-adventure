package com.jingyuyao.tactical.model.terrain;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.i18n.Message;
import com.jingyuyao.tactical.model.i18n.ModelBundle;

public class Water implements Terrain {

  @Override
  public Message getName() {
    return ModelBundle.TERRAIN_NAME.get("water");
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
