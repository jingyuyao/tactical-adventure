package com.jingyuyao.tactical.view.world2;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.google.common.util.concurrent.SettableFuture;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.view.resource.Animations;
import com.jingyuyao.tactical.view.resource.LoopAnimation;
import com.jingyuyao.tactical.view.world2.component.Moving;
import com.jingyuyao.tactical.view.world2.component.Position;
import com.jingyuyao.tactical.view.world2.component.Remove;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class CharacterEntities {

  private final WorldConfig worldConfig;
  private final PooledEngine engine;
  private final Map<Character, Entity> characterMap;
  private final Animations animations;

  @Inject
  CharacterEntities(
      WorldConfig worldConfig,
      PooledEngine engine,
      Map<Character, Entity> characterMap,
      Animations animations) {
    this.worldConfig = worldConfig;
    this.engine = engine;
    this.characterMap = characterMap;
    this.animations = animations;
  }

  void reset() {
    characterMap.clear();
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

  void move(Character character, Coordinate destination) {
    Entity entity = characterMap.get(character);
    entity.add(createPosition(destination));
  }

  void move(Character character, List<Coordinate> path, SettableFuture<Void> future) {
    Entity entity = characterMap.get(character);
    Moving moving = engine.createComponent(Moving.class);
    moving.setPath(path);
    moving.setFuture(future);
    entity.add(moving);
  }

  void remove(Character character) {
    Entity entity = characterMap.remove(character);
    entity.add(engine.createComponent(Remove.class));
  }

  private Position createPosition(Coordinate coordinate) {
    Position position = engine.createComponent(Position.class);
    position.setX(coordinate.getX() * worldConfig.getWorldUnit());
    position.setY(coordinate.getY() * worldConfig.getWorldUnit());
    position.setZ(WorldZIndex.CHARACTER);
    return position;
  }

  private LoopAnimation getAnimation(Character character) {
    return animations.getCharacter(character.getName());
  }
}
