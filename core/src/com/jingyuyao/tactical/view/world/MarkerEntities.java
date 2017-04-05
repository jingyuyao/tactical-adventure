package com.jingyuyao.tactical.view.world;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.view.resource.Markers;
import com.jingyuyao.tactical.view.resource.WorldTexture;
import com.jingyuyao.tactical.view.world.component.Position;
import com.jingyuyao.tactical.view.world.component.Remove;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class MarkerEntities {

  private final PooledEngine engine;
  private final Markers markers;
  private final Entity highlight;
  private final Entity activated;
  private final Family markedFamily;

  @Inject
  MarkerEntities(PooledEngine engine, Markers markers) {
    this.engine = engine;
    this.markers = markers;
    highlight = engine.createEntity();
    highlight.add(markers.getHighlight());
    engine.addEntity(highlight);
    activated = engine.createEntity();
    activated.add(markers.getActivated());
    engine.addEntity(activated);
    markedFamily = Family.all(Marked.class).get();
  }

  void highlight(Coordinate coordinate) {
    highlight.add(createPosition(coordinate, WorldZIndex.HIGHLIGHT_MARKER));
  }

  void activate(Coordinate coordinate) {
    activated.add(createPosition(coordinate, WorldZIndex.ACTIVATE_MARKER));
  }

  void deactivate() {
    activated.remove(Position.class);
  }

  void removeMarkers() {
    for (Entity entity : engine.getEntitiesFor(markedFamily)) {
      entity.add(engine.createComponent(Remove.class));
    }
  }

  void markMove(Coordinate coordinate) {
    mark(coordinate, markers.getMove(), WorldZIndex.MOVE_MARKER);
  }

  void markAttack(Coordinate coordinate) {
    mark(coordinate, markers.getAttack(), WorldZIndex.ATTACK_MARKER);
  }

  void markTargetSelect(Coordinate coordinate) {
    mark(coordinate, markers.getTargetSelect(), WorldZIndex.TARGET_SELECT_MARKER);
  }

  private void mark(Coordinate coordinate, WorldTexture worldTexture, int zIndex) {
    Entity entity = engine.createEntity();
    entity.add(createPosition(coordinate, zIndex));
    entity.add(worldTexture);
    entity.add(engine.createComponent(Marked.class));
    engine.addEntity(entity);
  }

  private Position createPosition(Coordinate coordinate, int zIndex) {
    Position position = engine.createComponent(Position.class);
    position.setX(coordinate.getX());
    position.setY(coordinate.getY());
    position.setZ(zIndex);
    return position;
  }

  private static class Marked implements Component {

  }
}
