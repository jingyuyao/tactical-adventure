package com.jingyuyao.tactical.view.world;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.LoopAnimation;
import com.jingyuyao.tactical.view.world.component.Position;
import com.jingyuyao.tactical.view.world.component.SingleAnimation;
import com.jingyuyao.tactical.view.world.resource.WorldTexture;
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

  Entity idle(Coordinate coordinate, int zIndex, WorldTexture worldTexture) {
    Entity entity = bare();
    entity.add(position(coordinate, zIndex));
    entity.add(frame(worldTexture));
    return entity;
  }

  Entity animated(Coordinate coordinate, int zIndex, LoopAnimation loopAnimation, Color color) {
    Entity entity = bare();
    entity.add(position(coordinate, zIndex));
    entity.add(frame(color));
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

  Frame frame(Color color) {
    Frame frame = component(Frame.class);
    frame.setColor(color);
    return frame;
  }

  Frame frame(WorldTexture texture) {
    Frame frame = component(Frame.class);
    frame.setTexture(texture);
    return frame;
  }
}
