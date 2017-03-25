package com.jingyuyao.tactical.view.actor;

import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.view.resource.WorldTexture;
import java.util.LinkedHashSet;

public class CellActor extends WorldActor<Cell> {

  CellActor(
      Cell object,
      Coordinate initialCoordinate,
      ActorConfig actorConfig,
      LinkedHashSet<WorldTexture> markers) {
    super(object, initialCoordinate, actorConfig, markers);
  }
}
