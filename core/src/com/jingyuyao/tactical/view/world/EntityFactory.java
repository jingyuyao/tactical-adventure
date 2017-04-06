package com.jingyuyao.tactical.view.world;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.view.resource.LoopAnimation;
import com.jingyuyao.tactical.view.resource.SingleAnimation;
import com.jingyuyao.tactical.view.resource.WorldTexture;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.Position;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * All methods adds the created {@link Entity} to the {@link PooledEngine}
 */
@Singleton
class EntityFactory {

  private final PooledEngine engine;

  @Inject
  EntityFactory(PooledEngine engine) {
    this.engine = engine;
  }

  Entity bare() {
    Entity entity = engine.createEntity();
    engine.addEntity(entity);
    return entity;
  }

  Entity animated(Coordinate coordinate, int zIndex, LoopAnimation loopAnimation) {
    Entity entity = bare();
    entity.add(position(coordinate, zIndex));
    entity.add(frame());
    entity.add(loopAnimation);
    return entity;
  }

  Entity animated(Coordinate coordinate, int zIndex, SingleAnimation singleAnimation) {
    Entity entity = bare();
    entity.add(position(coordinate, zIndex));
    entity.add(frame());
    entity.add(singleAnimation);
    return entity;
  }

  Entity idle(Coordinate coordinate, int zIndex, WorldTexture worldTexture) {
    Entity entity = bare();
    entity.add(position(coordinate, zIndex));
    entity.add(frame(worldTexture));
    return entity;
  }

  <T extends Component> T component(Class<T> clazz) {
    return engine.createComponent(clazz);
  }

  Position position(Coordinate coordinate, int zIndex) {
    Position position = component(Position.class);
    position.setX(coordinate.getX());
    position.setY(coordinate.getY());
    position.setZ(zIndex);
    return position;
  }

  Frame frame() {
    return component(Frame.class);
  }

  Frame frame(WorldTexture texture) {
    Frame frame = component(Frame.class);
    frame.setTexture(texture);
    return frame;
  }
}
