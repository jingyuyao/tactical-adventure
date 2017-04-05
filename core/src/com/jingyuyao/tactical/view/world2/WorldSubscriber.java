package com.jingyuyao.tactical.view.world2;

import com.badlogic.ashley.core.PooledEngine;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
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
import com.jingyuyao.tactical.view.resource.Animations;
import com.jingyuyao.tactical.view.resource.SingleAnimation;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldSubscriber {

  private final Entities entities;
  private final CharacterEntities characterEntities;
  private final MarkerEntities markerEntities;
  private final Animations animations;
  private final PooledEngine engine;

  @Inject
  WorldSubscriber(
      Entities entities,
      CharacterEntities characterEntities,
      MarkerEntities markerEntities,
      Animations animations,
      PooledEngine engine) {
    this.entities = entities;
    this.characterEntities = characterEntities;
    this.markerEntities = markerEntities;
    this.animations = animations;
    this.engine = engine;
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
//    CharacterActor actor = worldView.get(instantMoveCharacter.getCharacter());
//    actor.moveTo(instantMoveCharacter.getDestination().getCoordinate());
  }

  @Subscribe
  void moveCharacter(MoveCharacter moveCharacter) {
//    CharacterActor actor = worldView.get(moveCharacter.getCharacter());
//    actor.moveAlong(moveCharacter.getPath(), moveCharacter.getFuture());
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
    for (Target target : selectingTarget.getTargets()) {
      markTarget(target);
    }
  }

  @Subscribe
  void battling(Battling battling) {
    markTarget(battling.getTarget());
  }

  @Subscribe
  void exitState(ExitState exitState) {
    markerEntities.removeMarkers();
    markerEntities.deactivate();
  }

  @Subscribe
  void attack(final Attack attack) {
    SingleAnimation animation = animations.getWeapon(attack.getWeapon().getName());
    Futures.addCallback(animation.getFuture(), new FutureCallback<Void>() {
      @Override
      public void onSuccess(Void result) {
        attack.done();
      }

      @Override
      public void onFailure(Throwable t) {

      }
    });
    // TODO: need a way to distinguish on animation on select tile or an animation for every target
    // tile
//    for (Cell cell : attack.getObject().getSelectCells()) {
//      markings.addSingleAnimation(worldView.get(cell.getTerrain()), animation);
//    }
  }

  private void markTarget(Target target) {
    for (Cell cell : target.getTargetCells()) {
      markerEntities.markAttack(cell.getCoordinate());
    }
    for (Cell cell : target.getSelectCells()) {
      markerEntities.markTargetSelect(cell.getCoordinate());
    }
  }
}
