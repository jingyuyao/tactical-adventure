package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.Waiter;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.view.actor.ActorConfig.ActorWorldSize;
import java.util.Map;
import javax.inject.Inject;

// Note: Guice reflection needs all injectable classes to be public
public class TerrainActor extends BaseActor<Terrain> {

  @Inject
  TerrainActor(
      @Assisted Terrain object,
      @Assisted EventListener listener,
      @ActorWorldSize float size,
      EventBus eventBus,
      Waiter waiter,
      Map<Marker, Sprite> markerSpriteMap) {
    super(object, listener, size, eventBus, waiter, markerSpriteMap);
  }
}
