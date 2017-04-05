package com.jingyuyao.tactical.view.world2;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.event.ActivatedEnemy;
import com.jingyuyao.tactical.model.event.Attack;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.InstantMoveCharacter;
import com.jingyuyao.tactical.model.event.MoveCharacter;
import com.jingyuyao.tactical.model.event.RemoveCharacter;
import com.jingyuyao.tactical.model.event.SelectCell;
import com.jingyuyao.tactical.model.event.SpawnCharacter;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.state.Battling;
import com.jingyuyao.tactical.model.state.Moving;
import com.jingyuyao.tactical.model.state.PlayerState;
import com.jingyuyao.tactical.model.state.SelectingTarget;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldSubscriber {

  private final Entities entities;
  private final CharacterEntities characterEntities;
  private final MarkerEntities markerEntities;
  private final EffectsEntities effectsEntities;

  @Inject
  WorldSubscriber(
      Entities entities,
      CharacterEntities characterEntities,
      MarkerEntities markerEntities,
      EffectsEntities effectsEntities) {
    this.entities = entities;
    this.characterEntities = characterEntities;
    this.markerEntities = markerEntities;
    this.effectsEntities = effectsEntities;
  }

  @Subscribe
  void worldReset(WorldReset worldReset) {
    entities.reset();
  }

  @Subscribe
  void spawnCharacter(SpawnCharacter spawnCharacter) {
    Cell cell = spawnCharacter.getObject();
    Coordinate coordinate = cell.getCoordinate();
    if (cell.hasPlayer()) {
      characterEntities.add(coordinate, cell.getPlayer());
    } else if (cell.hasEnemy()) {
      characterEntities.add(coordinate, cell.getEnemy());
    }
  }

  @Subscribe
  void removeCharacter(RemoveCharacter removeCharacter) {
    characterEntities.remove(removeCharacter.getObject());
  }

  @Subscribe
  void instantMoveCharacter(InstantMoveCharacter instantMoveCharacter) {
    characterEntities.move(
        instantMoveCharacter.getCharacter(),
        instantMoveCharacter.getDestination().getCoordinate());
  }

  @Subscribe
  void moveCharacter(MoveCharacter moveCharacter) {
    characterEntities.move(
        moveCharacter.getCharacter(),
        FluentIterable
            .from(moveCharacter.getPath().getTrack())
            .transform(new Function<Cell, Coordinate>() {
              @Override
              public Coordinate apply(Cell input) {
                return input.getCoordinate();
              }
            }).toList(),
        moveCharacter.getFuture());
  }

  @Subscribe
  void selectCell(SelectCell selectCell) {
    Cell cell = selectCell.getObject();
    markerEntities.highlight(cell.getCoordinate());
  }

  @Subscribe
  void playerState(PlayerState playerState) {
//    markerEntities.activate(coordinate);
  }

  @Subscribe
  void activatedEnemy(ActivatedEnemy activatedEnemy) {
//    markerEntities.activate(coordinate);
  }

  @Subscribe
  void moving(Moving moving) {
    for (Cell cell : moving.getMovement().getCells()) {
      markerEntities.markMove(cell.getCoordinate());
    }
  }

  @Subscribe
  void selectingTarget(SelectingTarget selectingTarget) {
    Set<Cell> targetCells = new HashSet<>();
    Set<Cell> selectCells = new HashSet<>();
    for (Target target : selectingTarget.getTargets()) {
      targetCells.addAll(target.getTargetCells());
      selectCells.addAll(target.getSelectCells());
    }
    for (Cell cell : targetCells) {
      markerEntities.markAttack(cell.getCoordinate());
    }
    for (Cell cell : selectCells) {
      markerEntities.markTargetSelect(cell.getCoordinate());
    }
  }

  @Subscribe
  void battling(Battling battling) {
    Target target = battling.getTarget();
    for (Cell cell : target.getTargetCells()) {
      markerEntities.markAttack(cell.getCoordinate());
    }
    for (Cell cell : target.getSelectCells()) {
      markerEntities.markTargetSelect(cell.getCoordinate());
    }
  }

  @Subscribe
  void exitState(ExitState exitState) {
    markerEntities.removeMarkers();
    markerEntities.deactivate();
  }

  @Subscribe
  void attack(final Attack attack) {
    // TODO: distinguish on animation on select tile or an animation for every target tile
    Cell select = Iterables.getFirst(attack.getObject().getSelectCells(), null);
    if (select != null) {
      effectsEntities
          .addWeaponEffect(select.getCoordinate(), attack.getWeapon(), attack.getFuture());
    }
  }
}
