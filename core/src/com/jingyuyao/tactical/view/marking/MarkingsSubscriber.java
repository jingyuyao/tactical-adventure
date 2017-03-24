package com.jingyuyao.tactical.view.marking;

import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.jingyuyao.tactical.model.event.ActivatedEnemy;
import com.jingyuyao.tactical.model.event.Attack;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.SelectCell;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.state.Battling;
import com.jingyuyao.tactical.model.state.Moving;
import com.jingyuyao.tactical.model.state.PlayerState;
import com.jingyuyao.tactical.model.state.SelectingTarget;
import com.jingyuyao.tactical.view.resource.Animations;
import com.jingyuyao.tactical.view.resource.Markers;
import com.jingyuyao.tactical.view.resource.SingleAnimation;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MarkingsSubscriber {

  private final Markings markings;
  private final Markers markers;
  private final Animations animations;

  @Inject
  MarkingsSubscriber(Markings markings, Markers markers, Animations animations) {
    this.markings = markings;
    this.markers = markers;
    this.animations = animations;
  }

  @Subscribe
  void selectCell(SelectCell selectCell) {
    Cell cell = selectCell.getObject();
    markings.highlight(cell.getTerrain());
  }

  @Subscribe
  void playerState(PlayerState playerState) {
    markings.activate(playerState.getPlayer());
  }

  @Subscribe
  void activatedEnemy(ActivatedEnemy activatedEnemy) {
    markings.activate(activatedEnemy.getObject());
  }

  @Subscribe
  void moving(Moving moving) {
    for (Cell cell : moving.getMovement().getCells()) {
      markings.mark(cell.getTerrain(), markers.getMove());
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
    markings.clearMarked();
    markings.activate(null);
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
    for (Cell cell : attack.getObject().getSelectCells()) {
      markings.addSingleAnimation(cell.getTerrain(), animation);
    }
  }

  private void markTarget(Target target) {
    for (Cell cell : target.getTargetCells()) {
      markings.mark(cell.getTerrain(), markers.getAttack());
    }
    for (Cell cell : target.getSelectCells()) {
      markings.mark(cell.getTerrain(), markers.getTargetSelect());
    }
  }
}
