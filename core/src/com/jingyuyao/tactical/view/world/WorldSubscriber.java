package com.jingyuyao.tactical.view.world;

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
import com.jingyuyao.tactical.view.world.system.CharacterSystem;
import com.jingyuyao.tactical.view.world.system.EffectsSystem;
import com.jingyuyao.tactical.view.world.system.MarkerSystem;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldSubscriber {

  private final WorldEngine worldEngine;
  private final CharacterSystem characterSystem;
  private final MarkerSystem markerSystem;
  private final EffectsSystem effectsSystem;

  @Inject
  WorldSubscriber(
      WorldEngine worldEngine,
      CharacterSystem characterSystem,
      MarkerSystem markerSystem,
      EffectsSystem effectsSystem) {
    this.worldEngine = worldEngine;
    this.characterSystem = characterSystem;
    this.markerSystem = markerSystem;
    this.effectsSystem = effectsSystem;
  }

  @Subscribe
  void worldReset(WorldReset worldReset) {
    worldEngine.reset();
  }

  @Subscribe
  void spawnCharacter(SpawnCharacter spawnCharacter) {
    Cell cell = spawnCharacter.getObject();
    Coordinate coordinate = cell.getCoordinate();
    if (cell.hasPlayer()) {
      characterSystem.add(coordinate, cell.getPlayer());
    } else if (cell.hasEnemy()) {
      characterSystem.add(coordinate, cell.getEnemy());
    }
  }

  @Subscribe
  void removeCharacter(RemoveCharacter removeCharacter) {
    characterSystem.remove(removeCharacter.getObject());
  }

  @Subscribe
  void instantMoveCharacter(InstantMoveCharacter instantMoveCharacter) {
    characterSystem.move(
        instantMoveCharacter.getCharacter(),
        instantMoveCharacter.getDestination().getCoordinate());
  }

  @Subscribe
  void moveCharacter(MoveCharacter moveCharacter) {
    characterSystem.move(
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
    markerSystem.highlight(cell.getCoordinate());
  }

  @Subscribe
  void playerState(PlayerState playerState) {
    markerSystem.activate(characterSystem.get(playerState.getPlayer()));
  }

  @Subscribe
  void activatedEnemy(ActivatedEnemy activatedEnemy) {
    markerSystem.activate(characterSystem.get(activatedEnemy.getObject()));
  }

  @Subscribe
  void moving(Moving moving) {
    for (Cell cell : moving.getMovement().getCells()) {
      markerSystem.markMove(cell.getCoordinate());
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
      markerSystem.markAttack(cell.getCoordinate());
    }
    for (Cell cell : selectCells) {
      markerSystem.markTargetSelect(cell.getCoordinate());
    }
  }

  @Subscribe
  void battling(Battling battling) {
    Target target = battling.getTarget();
    for (Cell cell : target.getTargetCells()) {
      markerSystem.markAttack(cell.getCoordinate());
    }
    for (Cell cell : target.getSelectCells()) {
      markerSystem.markTargetSelect(cell.getCoordinate());
    }
  }

  @Subscribe
  void exitState(ExitState exitState) {
    markerSystem.removeMarkers();
  }

  @Subscribe
  void attack(final Attack attack) {
    // TODO: distinguish on animation on select tile or an animation for every target tile
    Cell select = Iterables.getFirst(attack.getObject().getSelectCells(), null);
    if (select != null) {
      effectsSystem
          .addWeaponEffect(select.getCoordinate(), attack.getWeapon(), attack.getFuture());
    }
  }
}
