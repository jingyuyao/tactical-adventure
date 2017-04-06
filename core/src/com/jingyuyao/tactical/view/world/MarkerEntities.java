package com.jingyuyao.tactical.view.world;

import com.badlogic.ashley.core.Entity;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.view.resource.Markers;
import com.jingyuyao.tactical.view.resource.WorldTexture;
import com.jingyuyao.tactical.view.world.WorldModule.MarkedEntityList;
import com.jingyuyao.tactical.view.world.component.Position;
import com.jingyuyao.tactical.view.world.component.Remove;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class MarkerEntities {

  private final EntityFactory entityFactory;
  private final Markers markers;
  private final Entity highlight;
  private final Entity activated;
  private final List<Entity> markedEntities;

  @Inject
  MarkerEntities(
      EntityFactory entityFactory,
      Markers markers,
      @MarkedEntityList List<Entity> markedEntities) {
    this.entityFactory = entityFactory;
    this.markers = markers;
    this.markedEntities = markedEntities;
    highlight = entityFactory.bare();
    highlight.add(entityFactory.frame(markers.getHighlight()));
    activated = entityFactory.bare();
    activated.add(entityFactory.frame(markers.getActivated()));
  }

  void highlight(Coordinate coordinate) {
    highlight.add(entityFactory.position(coordinate, WorldZIndex.HIGHLIGHT_MARKER));
  }

  void activate(Coordinate coordinate) {
    activated.add(entityFactory.position(coordinate, WorldZIndex.ACTIVATE_MARKER));
  }

  void deactivate() {
    activated.remove(Position.class);
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
