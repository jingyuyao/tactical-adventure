package com.jingyuyao.tactical.model.state;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.map.Path;
import com.jingyuyao.tactical.model.map.Targets;
import com.jingyuyao.tactical.model.map.Terrain;
import javax.inject.Inject;

class Moving extends AbstractPlayerState {

  private Coordinate previousCoordinate;

  @Inject
  Moving(EventBus eventBus, MapState mapState, StateFactory stateFactory, @Assisted Player player) {
    super(eventBus, mapState, stateFactory, player);
  }

  @Override
  public void enter() {
    super.enter();
    getPlayer().showAllTargetsWithMove();
  }

  @Override
  public void canceled() {
    if (previousCoordinate != null) {
      getPlayer().instantMoveTo(previousCoordinate);
      previousCoordinate = null;
    }
  }

  @Override
  public void select(Player player) {
    if (previousCoordinate != null) {
      return;
    }
    if (Objects.equal(getPlayer(), player)) {
      goTo(getStateFactory().createChoosing(getPlayer()));
    } else {
      rollback();
      goTo(getStateFactory().createMoving(player));
    }
  }

  @Override
  public void select(final Enemy enemy) {
    if (previousCoordinate != null) {
      return;
    }
    Targets playerTargets = getPlayer().createTargets();
    if (playerTargets.all().canTarget(enemy)) {
      Path path = playerTargets.movePathToTargetCoordinate(enemy.getCoordinate());
      moveCurrentPlayer(path, new Runnable() {
        @Override
        public void run() {
          // creates an intermediate choosing state so we can backtrack here if needed
          goTo(getStateFactory().createChoosing(getPlayer()));
          goTo(getStateFactory().createSelectingWeapon(getPlayer(), enemy));
        }
      });
    } else {
      back();
    }
  }

  @Override
  public void select(Terrain terrain) {
    if (previousCoordinate != null) {
      return;
    }
    Targets playerTargets = getPlayer().createTargets();
    if (playerTargets.canMoveTo(terrain.getCoordinate())) {
      Path path = playerTargets.pathTo(terrain.getCoordinate());
      moveCurrentPlayer(path, new Runnable() {
        @Override
        public void run() {
          goTo(getStateFactory().createChoosing(getPlayer()));
        }
      });
    } else {
      // we will consider clicking outside of movable area to be canceling
      back();
    }
  }

  @Override
  public ImmutableList<Action> getActions() {
    return ImmutableList.<Action>of(this.new Back() {
      @Override
      public void run() {
        if (previousCoordinate != null) {
          return;
        }
        super.run();
      }
    });
  }

  private void moveCurrentPlayer(Path path, Runnable onFinish) {
    previousCoordinate = getPlayer().getCoordinate();
    getPlayer().move(path, onFinish);
  }
}
