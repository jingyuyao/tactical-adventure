package com.jingyuyao.tactical.model.terrain;

import com.google.common.collect.Multiset;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.map.MapModule.InitialMarkers;
import com.jingyuyao.tactical.model.map.MapObjectData;
import com.jingyuyao.tactical.model.map.Marker;
import javax.inject.Inject;

public class Water extends AbstractTerrain {

  @Inject
  Water(@Assisted MapObjectData data, @InitialMarkers Multiset<Marker> markers) {
    super(data, markers);
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
