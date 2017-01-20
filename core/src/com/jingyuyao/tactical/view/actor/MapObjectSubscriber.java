package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.map.event.SyncMarkers;
import com.jingyuyao.tactical.model.mark.Marker;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MapObjectSubscriber {

  private final Actors actors;
  private final Map<Marker, Sprite> markerSpriteMap;

  @Inject
  MapObjectSubscriber(EventBus eventBus, Actors actors, Map<Marker, Sprite> markerSpriteMap) {
    this.actors = actors;
    this.markerSpriteMap = markerSpriteMap;
    eventBus.register(this);
  }

  @Subscribe
  public void markersChanged(SyncMarkers syncMarkers) {
    MapActor mapActor = actors.get(syncMarkers.getObject());

    // TODO: temp fix (really), sync marker could be invoked after object has died
    if (mapActor == null) {
      return;
    }

    mapActor.setMarkerSprites(
        Iterables.transform(syncMarkers.getMarkers(), new Function<Marker, Sprite>() {
          @Override
          public Sprite apply(Marker input) {
            return markerSpriteMap.get(input);
          }
        }));
  }
}
