package com.jingyuyao.tactical.view.world.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.MyFuture;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.view.world.component.CharacterComponent;
import com.jingyuyao.tactical.view.world.component.LoopAnimation;
import com.jingyuyao.tactical.view.world.component.Moving;
import com.jingyuyao.tactical.view.world.component.Remove;
import com.jingyuyao.tactical.view.world.resource.Animations;
import com.jingyuyao.tactical.view.world.resource.Colors;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CharacterSystem extends EntitySystem {

  private final EntityFactory entityFactory;
  private final ComponentMapper<CharacterComponent> characterMapper;
  private final Animations animations;
  private ImmutableArray<Entity> entities;

  @Inject
  CharacterSystem(
      EntityFactory entityFactory,
      ComponentMapper<CharacterComponent> characterMapper,
      Animations animations) {
    this.entityFactory = entityFactory;
    this.characterMapper = characterMapper;
    this.animations = animations;
    this.priority = SystemPriority.CHARACTER;
  }

  @Override
  public void addedToEngine(Engine engine) {
    entities = engine.getEntitiesFor(Family.all(CharacterComponent.class).get());
  }

  public void add(Coordinate coordinate, Player player) {
    Entity entity =
        entityFactory
            .animated(coordinate, WorldZIndex.CHARACTER, getAnimation(player), Colors.BLUE_300);
    entity.add(entityFactory.character(player));
    entity.add(entityFactory.player(player));
  }

  public void add(Coordinate coordinate, Enemy enemy) {
    Entity entity =
        entityFactory
            .animated(coordinate, WorldZIndex.CHARACTER, getAnimation(enemy), Colors.RED_500);
    entity.add(entityFactory.character(enemy));
  }

  /**
   * O(n).
   *
   * Could be constant time if we keep a map but then we would be holding references to entities
   * outside of the engine which is a bad practice.
   */
  public Entity get(final Character character) {
    return Iterables.find(entities, new Predicate<Entity>() {
      @Override
      public boolean apply(Entity input) {
        CharacterComponent component = characterMapper.get(input);
        return component != null && component.getCharacter().equals(character);
      }
    });
  }

  public void move(Character character, Coordinate destination) {
    Entity entity = get(character);
    entity.add(entityFactory.position(destination, WorldZIndex.CHARACTER));
  }

  public void move(Character character, List<Coordinate> path, MyFuture future) {
    Entity entity = get(character);
    Moving moving = entityFactory.component(Moving.class);
    moving.setPath(path);
    moving.setFuture(future);
    entity.add(moving);
  }

  public void remove(Character character) {
    Entity entity = get(character);
    entity.add(entityFactory.component(Remove.class));
  }

  private LoopAnimation getAnimation(Character character) {
    return animations.getCharacter(character.getName());
  }
}
