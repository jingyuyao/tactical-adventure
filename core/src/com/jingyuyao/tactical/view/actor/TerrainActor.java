package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.view.actor.ActorModule.ActorWorldSize;
import com.jingyuyao.tactical.view.actor.ActorModule.InitialMarkers;
import java.util.LinkedHashSet;
import javax.inject.Inject;

public class TerrainActor extends MapActor<Terrain> {

  @Inject
  TerrainActor(
      @Assisted Terrain object,
      @Assisted EventListener listener,
      @ActorWorldSize float size,
      @InitialMarkers LinkedHashSet<Sprite> markers) {
    super(object, listener, size, markers);
  }
}
