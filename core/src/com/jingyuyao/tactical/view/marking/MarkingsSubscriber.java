package com.jingyuyao.tactical.view.marking;

import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.jingyuyao.tactical.model.event.ActivatedEnemy;
import com.jingyuyao.tactical.model.event.Attack;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.SelectEnemy;
import com.jingyuyao.tactical.model.event.SelectPlayer;
import com.jingyuyao.tactical.model.event.SelectTerrain;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.state.Battling;
import com.jingyuyao.tactical.model.state.Moving;
import com.jingyuyao.tactical.model.state.PlayerState;
import com.jingyuyao.tactical.model.state.SelectingTarget;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.view.resource.Animations;
import com.jingyuyao.tactical.view.resource.MarkerSprites;
import com.jingyuyao.tactical.view.resource.SingleAnimation;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MarkingsSubscriber {

  private final Markings markings;
  private final MarkerSprites markerSprites;
  private final Animations animations;

  @Inject
  MarkingsSubscriber(Markings markings, MarkerSprites markerSprites, Animations animations) {
    this.markings = markings;
    this.markerSprites = markerSprites;
    this.animations = animations;
  }

  @Subscribe
  void selectPlayer(SelectPlayer selectPlayer) {
    markings.highlight(selectPlayer.getObject());
  }

  @Subscribe
  void selectEnemy(SelectEnemy selectEnemy) {
    markings.highlight(selectEnemy.getObject());
  }

  @Subscribe
  void selectTerrain(SelectTerrain selectTerrain) {
    markings.highlight(selectTerrain.getObject());
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
    for (Terrain terrain : moving.getMovement().getTerrains()) {
      markings.mark(terrain, markerSprites.getMove());
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
    for (MapObject object : attack.getObject().getHitObjects()) {
      markings.addSingleAnimation(object, animation);
    }
  }

  private void markTarget(Target target) {
    for (Terrain terrain : target.getTargetTerrains()) {
      markings.mark(terrain, markerSprites.getAttack());
    }
    for (Terrain terrain : target.getSelectTerrains()) {
      markings.mark(terrain, markerSprites.getTargetSelect());
    }
  }
}
