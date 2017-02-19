package com.jingyuyao.tactical.view.world;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.view.world.WorldModule.ActorSize;
import com.jingyuyao.tactical.view.world.WorldModule.InitialMarkers;
import java.util.LinkedHashSet;
import javax.inject.Inject;

class TerrainActor extends MapActor<Terrain> {

  @Inject
  TerrainActor(
      @Assisted Terrain object,
      @ActorSize float size,
      @InitialMarkers LinkedHashSet<Sprite> markers) {
    super(object, size, markers);
    setZIndex(0);
  }
}
