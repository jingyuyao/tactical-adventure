package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.common.EventSubscriber;
import com.jingyuyao.tactical.model.event.ClearMap;
import com.jingyuyao.tactical.model.event.NewMap;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.state.event.HighlightCharacter;
import com.jingyuyao.tactical.model.state.event.HighlightTerrain;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HighlightSubscriber implements EventSubscriber {

  private final Actors actors;
  private final Map<Marker, Sprite> markerSpriteMap;
  private MapActor currentHighlight;

  @Inject
  HighlightSubscriber(Actors actors, Map<Marker, Sprite> markerSpriteMap) {
    this.actors = actors;
    this.markerSpriteMap = markerSpriteMap;
  }

  @Subscribe
  public void initialize(NewMap data) {
    currentHighlight = null;
  }

  @Subscribe
  public void dispose(ClearMap data) {
    currentHighlight = null;
  }

  @Subscribe
  public void highlightCharacter(HighlightCharacter highlightCharacter) {
    switchHighlightTo(highlightCharacter.getObject());
  }

  @Subscribe
  public void highlightTerrain(HighlightTerrain highlightTerrain) {
    switchHighlightTo(highlightTerrain.getObject());
  }

  private void switchHighlightTo(MapObject highlight) {
    if (currentHighlight != null) {
      currentHighlight.removeMarkerSprite(markerSpriteMap.get(Marker.HIGHLIGHT));
    }
    currentHighlight = actors.get(highlight);
    if (currentHighlight != null) {
      currentHighlight.addMarkerSprite(markerSpriteMap.get(Marker.HIGHLIGHT));
    }
  }
}
