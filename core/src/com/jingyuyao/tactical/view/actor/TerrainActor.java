package com.jingyuyao.tactical.view.actor;

import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.view.actor.ActorModule.InitialMarkers;
import com.jingyuyao.tactical.view.resource.WorldTexture;
import java.util.LinkedHashSet;
import javax.inject.Inject;

public class TerrainActor extends WorldActor<Terrain> {

  @Inject
  TerrainActor(
      @Assisted Terrain object,
      ActorConfig actorConfig,
      @InitialMarkers LinkedHashSet<WorldTexture> markers) {
    super(object, actorConfig, markers);
  }
}
