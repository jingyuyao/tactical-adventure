package com.jingyuyao.tactical.view.world.system;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.view.world.component.CharacterComponent;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.PlayerComponent;
import com.jingyuyao.tactical.view.world.component.Position;
import com.jingyuyao.tactical.view.world.resource.WorldTexture;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Entity component factory.
 */
@Singleton
@Deprecated
class ECF {

  private final Engine engine;

  @Inject
  ECF(Engine engine) {
    this.engine = engine;
  }

  /**
   * Creates an {@link Entity} and adds it to the {@link Engine}.
   */
  Entity entity() {
    Entity entity = engine.createEntity();
    engine.addEntity(entity);
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

  CharacterComponent character(Character character) {
    CharacterComponent characterComponent = component(CharacterComponent.class);
    characterComponent.setCharacter(character);
    return characterComponent;
  }

  PlayerComponent player(Player player) {
    PlayerComponent playerComponent = component(PlayerComponent.class);
    playerComponent.setPlayer(player);
    return playerComponent;
  }
}
