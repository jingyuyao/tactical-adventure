package com.jingyuyao.tactical.view.world.system;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.view.world.WorldZIndex;
import com.jingyuyao.tactical.view.world.component.Frame;
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
  private ImmutableArray<Entity> marked;
  private ImmutableArray<Entity> highlight;
  private ImmutableArray<Entity> activated;

  @Inject
  MarkerSystem(
      EntityFactory entityFactory,
      Markers markers,
      ComponentMapper<Frame> frameMapper) {
    this.entityFactory = entityFactory;
    this.markers = markers;
    this.frameMapper = frameMapper;
    this.priority = SystemPriority.MARKER;
  }

  @Override
  public void addedToEngine(Engine engine) {
    marked = engine.getEntitiesFor(Family.all(Marked.class).get());
    highlight = engine.getEntitiesFor(Family.all(Highlight.class).get());
    activated = engine.getEntitiesFor(Family.all(Activated.class).get());
  }

  public void highlight(Coordinate coordinate) {
    Entity entity;
    if (highlight.size() == 0) {
      entity = entityFactory.bare();
      entity.add(entityFactory.frame(markers.getHighlight()));
      entity.add(entityFactory.component(Highlight.class));
    } else {
      entity = highlight.first();
    }
    entity.add(entityFactory.position(coordinate, WorldZIndex.HIGHLIGHT_MARKER));
  }

  public void activate(Entity entity) {
    Preconditions.checkArgument(frameMapper.has(entity));
    deactivate();
    Frame frame = frameMapper.get(entity);
    frame.addOverlay(markers.getActivated());
    entity.add(entityFactory.component(Activated.class));
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
    for (Entity entity : marked) {
      entity.add(entityFactory.component(Remove.class));
    }
    deactivate();
  }

  private void mark(Coordinate coordinate, int zIndex, WorldTexture worldTexture) {
    Entity entity = entityFactory.idle(coordinate, zIndex, worldTexture);
    entity.add(entityFactory.component(Marked.class));
  }

  private void deactivate() {
    for (Entity entity : activated) {
      Frame frame = frameMapper.get(entity);
      frame.removeOverlay(markers.getActivated());
      entity.remove(Activated.class);
    }
  }

  private static class Marked implements Component, Poolable {

    @Override
    public void reset() {

    }
  }

  private static class Highlight implements Component, Poolable {

    @Override
    public void reset() {

    }
  }

  private static class Activated implements Component, Poolable {

    @Override
    public void reset() {

    }
  }
}
