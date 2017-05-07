package com.jingyuyao.tactical.model.terrain;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.i18n.Message;
import com.jingyuyao.tactical.model.i18n.ModelBundle;

public class Land implements Terrain {

  @Override
  public Message getName() {
    return ModelBundle.TERRAIN_NAME.get("land");
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
