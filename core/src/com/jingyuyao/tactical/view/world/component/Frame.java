package com.jingyuyao.tactical.view.world.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.view.resource.WorldTexture;
import java.util.ArrayList;
import java.util.List;

/**
 * A frame has a main texture and a list of overlaying textures on top of it.
 */
public class Frame implements Component, Poolable {

  private final List<WorldTexture> overlays = new ArrayList<>(4);
  private WorldTexture texture = null;

  public Optional<WorldTexture> getTexture() {
    return Optional.fromNullable(texture);
  }

  public void setTexture(WorldTexture texture) {
    this.texture = texture;
  }

  public List<WorldTexture> getOverlays() {
    return overlays;
  }

  public void addOverlay(WorldTexture texture) {
    overlays.add(texture);
  }

  public void removeOverlay(WorldTexture texture) {
    overlays.remove(texture);
  }

  @Override
  public void reset() {
    texture = null;
    overlays.clear();
  }
}
