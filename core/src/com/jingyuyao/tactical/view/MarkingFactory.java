package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Movement;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.view.actor.MapActor;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class MarkingFactory {

  private final Map<MapObject, MapActor<?>> actorMap;
  private final Map<Marker, Sprite> markerSpriteMap;

  @Inject
  MarkingFactory(Map<MapObject, MapActor<?>> actorMap, Map<Marker, Sprite> markerSpriteMap) {
    this.actorMap = actorMap;
    this.markerSpriteMap = markerSpriteMap;
  }

  Multimap<MapActor, Sprite> createMovement(Movement movement) {
    Multimap<MapActor, Sprite> multimap = HashMultimap.create();
    for (Terrain terrain : movement.getTerrains()) {
      multimap.put(actorMap.get(terrain), markerSpriteMap.get(Marker.CAN_MOVE_TO));
    }
    return multimap;
  }

  Multimap<MapActor, Sprite> createTarget(Target target) {
    Multimap<MapActor, Sprite> multimap = HashMultimap.create();
    for (Terrain terrain : target.getSelectTerrains()) {
      multimap.put(actorMap.get(terrain), markerSpriteMap.get(Marker.TARGET_SELECT));
    }
    for (Terrain terrain : target.getTargetTerrains()) {
      multimap.put(actorMap.get(terrain), markerSpriteMap.get(Marker.CAN_ATTACK));
    }
    return multimap;
  }

  Multimap<MapActor, Sprite> createHit(Target target) {
    Multimap<MapActor, Sprite> multimap = HashMultimap.create();
    for (MapObject object : target.getHitObjects()) {
      multimap.put(actorMap.get(object), markerSpriteMap.get(Marker.HIT));
    }
    return multimap;
  }
}
