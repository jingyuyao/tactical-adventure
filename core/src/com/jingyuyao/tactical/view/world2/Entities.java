package com.jingyuyao.tactical.view.world2;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.view.resource.Animations;
import com.jingyuyao.tactical.view.resource.LoopAnimation;
import com.jingyuyao.tactical.view.world2.component.Position;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class Entities {

  private final WorldConfig worldConfig;
  private final PooledEngine engine;
  private final Map<Character, Entity> characterMap;
  private final Animations animations;

  @Inject
  Entities(
      WorldConfig worldConfig,
      PooledEngine engine,
      Map<Character, Entity> characterMap,
      Animations animations) {
    this.worldConfig = worldConfig;
    this.engine = engine;
    this.characterMap = characterMap;
    this.animations = animations;
  }

  void update(float delta) {
    engine.update(delta);
  }

  void reset() {
    characterMap.clear();
    engine.removeAllEntities();
  }

  void add(Coordinate coordinate, Player player) {
    Entity entity = engine.createEntity();
    entity.add(createPosition(coordinate));
    entity.add(getAnimation(player));
    characterMap.put(player, entity);
    engine.addEntity(entity);
  }

  void add(Coordinate coordinate, Enemy enemy) {
    Entity entity = engine.createEntity();
    entity.add(createPosition(coordinate));
    entity.add(getAnimation(enemy));
    characterMap.put(enemy, entity);
    engine.addEntity(entity);
  }

  void remove(Character character) {
    Entity entity = characterMap.remove(character);
    engine.removeEntity(entity);
  }

  private Position createPosition(Coordinate coordinate) {
    Position position = engine.createComponent(Position.class);
    position.setX(coordinate.getX() * worldConfig.getWorldUnit());
    position.setY(coordinate.getY() * worldConfig.getWorldUnit());
    return position;
  }

  private LoopAnimation getAnimation(Character character) {
    return animations.getCharacter(character.getName());
  }
}
