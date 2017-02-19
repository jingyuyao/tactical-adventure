package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.view.actor.ActorModule.ActorSize;
import com.jingyuyao.tactical.view.actor.ActorModule.InitialMarkers;
import java.util.LinkedHashSet;
import javax.inject.Inject;

public class TerrainActor extends WorldActor<Terrain> {

  @Inject
  TerrainActor(
      @Assisted Terrain object,
      @ActorSize float size,
      @InitialMarkers LinkedHashSet<Sprite> markers) {
    super(object, size, markers);
    setZIndex(0);
  }
}
