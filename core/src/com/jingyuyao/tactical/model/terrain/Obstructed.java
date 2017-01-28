package com.jingyuyao.tactical.model.terrain;

import com.google.common.collect.Multiset;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.MapModule.InitialMarkers;
import com.jingyuyao.tactical.model.map.Marker;
import javax.inject.Inject;

public class Obstructed extends AbstractTerrain {

  @Inject
  Obstructed(@Assisted Coordinate coordinate, @InitialMarkers Multiset<Marker> markers) {
    super(coordinate, markers);
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
