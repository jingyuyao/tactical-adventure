package com.jingyuyao.tactical.view.world2;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.view.resource.Markers;
import com.jingyuyao.tactical.view.resource.WorldTexture;
import com.jingyuyao.tactical.view.world2.component.Position;
import com.jingyuyao.tactical.view.world2.component.Remove;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class MarkerEntities {

  private final WorldConfig worldConfig;
  private final PooledEngine engine;
  private final Markers markers;
  private final Entity highlight;
  private final Entity activated;
  private final Family markedFamily;

  @Inject
  MarkerEntities(WorldConfig worldConfig, PooledEngine engine, Markers markers) {
    this.worldConfig = worldConfig;
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
    highlight.add(createPosition(coordinate));
  }

  void activate(Coordinate coordinate) {
    activated.add(createPosition(coordinate));
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
    mark(coordinate, markers.getMove());
  }

  void markAttack(Coordinate coordinate) {
    mark(coordinate, markers.getAttack());
  }

  void markTargetSelect(Coordinate coordinate) {
    mark(coordinate, markers.getTargetSelect());
  }

  private void mark(Coordinate coordinate, WorldTexture worldTexture) {
    // TODO: need to maintain order and number of textures
    Entity entity = engine.createEntity();
    entity.add(createPosition(coordinate));
    entity.add(worldTexture);
    entity.add(engine.createComponent(Marked.class));
    engine.addEntity(entity);
  }

  private Position createPosition(Coordinate coordinate) {
    Position position = engine.createComponent(Position.class);
    position.setX(coordinate.getX() * worldConfig.getWorldUnit());
    position.setY(coordinate.getY() * worldConfig.getWorldUnit());
    return position;
  }

  private static class Marked implements Component {

  }
}
