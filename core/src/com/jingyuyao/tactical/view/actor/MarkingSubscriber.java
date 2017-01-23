package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.common.EventSubscriber;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.mark.event.HideMarking;
import com.jingyuyao.tactical.model.mark.event.ShowMarking;
import java.util.Map;
import java.util.Map.Entry;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MarkingSubscriber implements EventSubscriber {

  private final Actors actors;
  private final Map<Marker, Sprite> markerSpriteMap;

  @Inject
  MarkingSubscriber(Actors actors, Map<Marker, Sprite> markerSpriteMap) {
    this.actors = actors;
    this.markerSpriteMap = markerSpriteMap;
  }

  @Subscribe
  public void showMarking(ShowMarking showMarking) {
    for (Entry<MapObject, Marker> entry : showMarking.getObject().entrySet()) {
      MapActor actor = actors.get(entry.getKey());
      if (actor != null) {
        actor.addMarkerSprite(markerSpriteMap.get(entry.getValue()));
      }
    }
  }

  @Subscribe
  public void hideMarking(HideMarking hideMarking) {
    for (Entry<MapObject, Marker> entry : hideMarking.getObject().entrySet()) {
      MapActor actor = actors.get(entry.getKey());
      if (actor != null) {
        actor.removeMarkerSprite(markerSpriteMap.get(entry.getValue()));
      }
    }
  }
}
