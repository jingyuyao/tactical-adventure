package com.jingyuyao.tactical.view.world.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.view.world.WorldZIndex;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.Marked;
import com.jingyuyao.tactical.view.world.component.Remove;
import com.jingyuyao.tactical.view.world.resource.Markers;
import com.jingyuyao.tactical.view.world.resource.WorldTexture;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MarkerSystem extends EntitySystem {

  private final EntityFactory entityFactory;
  private final Markers markers;
  private final ComponentMapper<Frame> frameMapper;
  private final Entity highlight;
  private ImmutableArray<Entity> entities;
  private Entity activated;

  @Inject
  MarkerSystem(
      EntityFactory entityFactory,
      Markers markers,
      ComponentMapper<Frame> frameMapper) {
    this.entityFactory = entityFactory;
    this.markers = markers;
    this.frameMapper = frameMapper;
    highlight = entityFactory.bare();
    highlight.add(entityFactory.frame(markers.getHighlight()));
  }

  @Override
  public void addedToEngine(Engine engine) {
    entities = engine.getEntitiesFor(Family.all(Marked.class).get());
  }

  public void highlight(Coordinate coordinate) {
    highlight.add(entityFactory.position(coordinate, WorldZIndex.HIGHLIGHT_MARKER));
  }

  public void activate(Entity entity) {
    Preconditions.checkArgument(frameMapper.has(entity));
    deactivate();
    Frame frame = frameMapper.get(entity);
    frame.addOverlay(markers.getActivated());
    activated = entity;
  }

  public void deactivate() {
    if (activated != null) {
      Frame frame = frameMapper.get(activated);
      frame.removeOverlay(markers.getActivated());
    }
  }

  public void markMove(Coordinate coordinate) {
    mark(coordinate, WorldZIndex.MOVE_MARKER, markers.getMove());
  }

  public void markAttack(Coordinate coordinate) {
    mark(coordinate, WorldZIndex.ATTACK_MARKER, markers.getAttack());
  }

  public void markTargetSelect(Coordinate coordinate) {
    mark(coordinate, WorldZIndex.TARGET_SELECT_MARKER, markers.getTargetSelect());
  }

  public void removeMarkers() {
    for (Entity entity : entities) {
      entity.add(entityFactory.component(Remove.class));
    }
  }

  private void mark(Coordinate coordinate, int zIndex, WorldTexture worldTexture) {
    Entity entity = entityFactory.idle(coordinate, zIndex, worldTexture);
    entity.add(entityFactory.component(Marked.class));
  }
}
