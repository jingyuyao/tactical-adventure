package com.jingyuyao.tactical.view.world.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.world.Direction;
import com.jingyuyao.tactical.view.world.resource.WorldTexture;
import java.util.ArrayList;
import java.util.List;

/**
 * A frame has a main texture and a list of overlaying textures on top of it.
 */
public class Frame implements Component, Poolable {

  private final List<WorldTexture> overlays = new ArrayList<>(4);
  private WorldTexture texture = null;
  private Color color = Color.WHITE;
  private Direction direction = null;

  public Optional<WorldTexture> texture() {
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

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
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
    color = Color.WHITE;
    direction = null;
  }
}
