package com.jingyuyao.tactical.view.world.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.InstantMoveCharacter;
import com.jingyuyao.tactical.model.event.MoveCharacter;
import com.jingyuyao.tactical.model.event.RemoveCharacter;
import com.jingyuyao.tactical.model.event.SpawnCharacter;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.view.world.component.CharacterComponent;
import com.jingyuyao.tactical.view.world.component.Moving;
import com.jingyuyao.tactical.view.world.component.Remove;
import com.jingyuyao.tactical.view.world.resource.Animations;
import com.jingyuyao.tactical.view.world.resource.Colors;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class CharacterSystem extends EntitySystem {

  private final ECF ecf;
  private final ComponentMapper<CharacterComponent> characterMapper;
  private final Animations animations;
  private ImmutableArray<Entity> entities;

  @Inject
  CharacterSystem(
      ECF ecf,
      ComponentMapper<CharacterComponent> characterMapper,
      Animations animations) {
    super(SystemPriority.CHARACTER);
    this.ecf = ecf;
    this.characterMapper = characterMapper;
    this.animations = animations;
  }

  @Override
  public void addedToEngine(Engine engine) {
    entities = engine.getEntitiesFor(Family.all(CharacterComponent.class).get());
  }

  /**
   * O(n).
   *
   * Could be constant time if we keep a map but then we would be holding references to entities
   * outside of the engine which is a bad practice.
   */
  Entity get(final Character character) {
    return Iterables.find(entities, new Predicate<Entity>() {
      @Override
      public boolean apply(Entity input) {
        CharacterComponent component = characterMapper.get(input);
        return component != null && component.getCharacter().equals(character);
      }
    });
  }

  @Subscribe
  void spawnCharacter(SpawnCharacter spawnCharacter) {
    Cell cell = spawnCharacter.getObject();
    Entity entity = ecf.entity();
    entity.add(ecf.position(cell.getCoordinate(), WorldZIndex.CHARACTER));
    entity.add(animations.getCharacter(cell.getCharacter().getName()));
    entity.add(ecf.character(cell.getCharacter()));
    if (cell.hasPlayer()) {
      entity.add(ecf.frame(Colors.BLUE_300));
      entity.add(ecf.player(cell.getPlayer()));
    } else if (cell.hasEnemy()) {
      entity.add(ecf.frame(Colors.RED_500));
    }
  }

  @Subscribe
  void removeCharacter(RemoveCharacter removeCharacter) {
    Entity entity = get(removeCharacter.getObject());
    entity.add(ecf.component(Remove.class));
  }

  @Subscribe
  void instantMoveCharacter(InstantMoveCharacter instantMoveCharacter) {
    Entity entity = get(instantMoveCharacter.getCharacter());
    Coordinate destination = instantMoveCharacter.getDestination().getCoordinate();
    entity.add(ecf.position(destination, WorldZIndex.CHARACTER));
  }

  @Subscribe
  void moveCharacter(MoveCharacter moveCharacter) {
    Entity entity = get(moveCharacter.getCharacter());
    Moving moving = ecf.component(Moving.class);
    moving.setPath(
        FluentIterable
            .from(moveCharacter.getPath().getTrack())
            .transform(new Function<Cell, Coordinate>() {
              @Override
              public Coordinate apply(Cell input) {
                return input.getCoordinate();
              }
            }).toList());
    moving.setFuture(moveCharacter.getFuture());
    entity.add(moving);
  }
}
