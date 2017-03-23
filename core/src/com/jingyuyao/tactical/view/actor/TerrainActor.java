package com.jingyuyao.tactical.view.actor;

import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.view.actor.ActorModule.InitialMarkers;
import com.jingyuyao.tactical.view.resource.WorldTexture;
import java.util.LinkedHashSet;
import javax.inject.Inject;

public class TerrainActor extends WorldActor<Terrain> {

  @Inject
  TerrainActor(
      @Assisted Terrain object,
      @Assisted Coordinate initialCoordinate,
      ActorConfig actorConfig,
      @InitialMarkers LinkedHashSet<WorldTexture> markers) {
    super(object, initialCoordinate, actorConfig, markers);
  }
}
