package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.view.resource.WorldTexture;
import java.util.LinkedHashSet;

public class WorldActor extends Actor {

  private final LinkedHashSet<WorldTexture> markers;

  WorldActor(LinkedHashSet<WorldTexture> markers) {
    this.markers = markers;
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    for (WorldTexture worldTexture : markers) {
      worldTexture.draw(batch, this);
    }
  }

  public void addMarker(WorldTexture worldTexture) {
    markers.add(worldTexture);
  }

  public void clearMarkers() {
    markers.clear();
  }

  public void moveTo(Coordinate coordinate) {
    setPosition(coordinate.getX() * getWidth(), coordinate.getY() * getHeight());
  }
}
