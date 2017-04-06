package com.jingyuyao.tactical.view.world;

import com.badlogic.ashley.core.Entity;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.MyFuture;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.view.world.WorldModule.CharacterEntityMap;
import com.jingyuyao.tactical.view.world.component.LoopAnimation;
import com.jingyuyao.tactical.view.world.component.Moving;
import com.jingyuyao.tactical.view.world.component.Remove;
import com.jingyuyao.tactical.view.world.resource.Animations;
import com.jingyuyao.tactical.view.world.resource.Colors;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class CharacterEntities {

  private final EntityFactory entityFactory;
  private final Map<Character, Entity> characterMap;
  private final Animations animations;

  @Inject
  CharacterEntities(
      EntityFactory entityFactory,
      @CharacterEntityMap Map<Character, Entity> characterMap,
      Animations animations) {
    this.entityFactory = entityFactory;
    this.characterMap = characterMap;
    this.animations = animations;
  }

  void reset() {
    characterMap.clear();
  }

  void add(Coordinate coordinate, Player player) {
    Entity entity =
        entityFactory
            .animated(coordinate, WorldZIndex.CHARACTER, getAnimation(player), Colors.BLUE_300);
    entity.add(entityFactory.player(player));
    characterMap.put(player, entity);
  }

  void add(Coordinate coordinate, Enemy enemy) {
    Entity entity =
        entityFactory
            .animated(coordinate, WorldZIndex.CHARACTER, getAnimation(enemy), Colors.RED_500);
    characterMap.put(enemy, entity);
  }

  Entity get(Character character) {
    return characterMap.get(character);
  }

  void move(Character character, Coordinate destination) {
    Entity entity = characterMap.get(character);
    entity.add(entityFactory.position(destination, WorldZIndex.CHARACTER));
  }

  void move(Character character, List<Coordinate> path, MyFuture future) {
    Entity entity = characterMap.get(character);
    Moving moving = entityFactory.component(Moving.class);
    moving.setPath(path);
    moving.setFuture(future);
    entity.add(moving);
  }

  void remove(Character character) {
    Entity entity = characterMap.remove(character);
    entity.add(entityFactory.component(Remove.class));
  }

  private LoopAnimation getAnimation(Character character) {
    return animations.getCharacter(character.getName());
  }
}
