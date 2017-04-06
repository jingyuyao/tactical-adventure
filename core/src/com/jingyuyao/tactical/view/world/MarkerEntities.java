package com.jingyuyao.tactical.view.world;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.view.world.WorldModule.MarkedEntityList;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.Remove;
import com.jingyuyao.tactical.view.world.resource.Markers;
import com.jingyuyao.tactical.view.world.resource.WorldTexture;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class MarkerEntities {

  private final EntityFactory entityFactory;
  private final Markers markers;
  private final List<Entity> markedEntities;
  private final Entity highlight;
  private final ComponentMapper<Frame> frameMapper;
  private Entity activated;

  @Inject
  MarkerEntities(
      EntityFactory entityFactory,
      Markers markers,
      @MarkedEntityList List<Entity> markedEntities,
      ComponentMapper<Frame> frameMapper) {
    this.entityFactory = entityFactory;
    this.markers = markers;
    this.markedEntities = markedEntities;
    highlight = entityFactory.bare();
    this.frameMapper = frameMapper;
    highlight.add(entityFactory.frame(markers.getHighlight()));
  }

  void highlight(Coordinate coordinate) {
    highlight.add(entityFactory.position(coordinate, WorldZIndex.HIGHLIGHT_MARKER));
  }

  void activate(Entity entity) {
    Preconditions.checkArgument(frameMapper.has(entity));
    deactivate();
    Frame frame = frameMapper.get(entity);
    frame.addOverlay(markers.getActivated());
    activated = entity;
  }

  void deactivate() {
    if (activated != null) {
      Frame frame = frameMapper.get(activated);
      frame.removeOverlay(markers.getActivated());
    }
  }

  void removeMarkers() {
    for (Entity entity : markedEntities) {
      entity.add(entityFactory.component(Remove.class));
    }
    markedEntities.clear();
  }

  void markMove(Coordinate coordinate) {
    mark(coordinate, WorldZIndex.MOVE_MARKER, markers.getMove());
  }

  void markAttack(Coordinate coordinate) {
    mark(coordinate, WorldZIndex.ATTACK_MARKER, markers.getAttack());
  }

  void markTargetSelect(Coordinate coordinate) {
    mark(coordinate, WorldZIndex.TARGET_SELECT_MARKER, markers.getTargetSelect());
  }

  private void mark(Coordinate coordinate, int zIndex, WorldTexture worldTexture) {
    Entity entity = entityFactory.idle(coordinate, zIndex, worldTexture);
    markedEntities.add(entity);
  }
}
