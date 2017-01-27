package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.map.Marker;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.view.actor.ActorModule.ActorWorldSize;
import java.util.Map;
import javax.inject.Inject;

public class TerrainActor extends MapActor<Terrain> {

  @Inject
  TerrainActor(
      @Assisted Terrain object,
      @Assisted EventListener listener,
      @ActorWorldSize float size,
      Map<Marker, Sprite> markerSpriteMap) {
    super(object, listener, size, markerSpriteMap);
  }
}
