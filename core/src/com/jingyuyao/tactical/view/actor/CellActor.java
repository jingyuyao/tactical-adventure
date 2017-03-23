package com.jingyuyao.tactical.view.actor;

import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.view.actor.ActorModule.InitialMarkers;
import com.jingyuyao.tactical.view.resource.WorldTexture;
import java.util.LinkedHashSet;
import javax.inject.Inject;

public class CellActor extends WorldActor<Cell> {

  @Inject
  CellActor(
      @Assisted Cell object,
      @Assisted Coordinate initialCoordinate,
      ActorConfig actorConfig,
      @InitialMarkers LinkedHashSet<WorldTexture> markers) {
    super(object, initialCoordinate, actorConfig, markers);
  }
}
