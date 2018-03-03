package com.jingyuyao.tactical.view.world.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.world.Direction;
import com.jingyuyao.tactical.view.world.resource.WorldTexture;
import java.util.Map;
import java.util.TreeMap;

/**
 * A frame has a main texture and a list of overlaying textures on top of it.
 */
public class Frame implements Component, Poolable {

  private final Map<Integer, WorldTexture> overlays = new TreeMap<>();
  private WorldTexture texture = null;
  private Direction direction = null;

  public Optional<WorldTexture> texture() {
    return Optional.fromNullable(texture);
  }

  public void setTexture(WorldTexture texture) {
    this.texture = texture;
  }

  public Iterable<WorldTexture> getOverlays() {
    return overlays.values();
  }

  /**
   * Sets an overlay for this frame. The render order of the overlays is determined by the natural
   * ordering of the key.
   */
  public void setOverlay(int key, WorldTexture texture) {
    overlays.put(key, texture);
  }

  public void removeOverlay(int key) {
    overlays.remove(key);
  }

  public Optional<Direction> direction() {
    return Optional.fromNullable(direction);
  }

  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  @Override
  public void reset() {
    overlays.clear();
    texture = null;
    direction = null;
  }
}
