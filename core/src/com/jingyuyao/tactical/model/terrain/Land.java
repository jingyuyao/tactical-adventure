package com.jingyuyao.tactical.model.terrain;

import com.google.common.collect.Multiset;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.map.MapModule.InitialMarkers;
import com.jingyuyao.tactical.model.map.MapObjectData;
import com.jingyuyao.tactical.model.map.Marker;
import javax.inject.Inject;

public class Land extends AbstractTerrain {

  @Inject
  Land(@Assisted MapObjectData data, @InitialMarkers Multiset<Marker> markers) {
    super(data, markers);
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
