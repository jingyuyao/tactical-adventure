package com.jingyuyao.tactical.view.marking;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.event.ActivatedEnemy;
import com.jingyuyao.tactical.model.event.Attack;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.SelectObject;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.state.Battling;
import com.jingyuyao.tactical.model.state.Moving;
import com.jingyuyao.tactical.model.state.PlayerState;
import com.jingyuyao.tactical.model.state.SelectingTarget;
import com.jingyuyao.tactical.model.terrain.Terrain;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MarkingsSubscriber {

  private final Markings markings;
  private final MarkerSprites markerSprites;

  @Inject
  MarkingsSubscriber(Markings markings, MarkerSprites markerSprites) {
    this.markings = markings;
    this.markerSprites = markerSprites;
  }

  @Subscribe
  public void selectObject(SelectObject<MapObject> selectObject) {
    markings.highlight(selectObject.getObject());
  }

  @Subscribe
  public void playerState(PlayerState playerState) {
    markings.activate(playerState.getPlayer());
  }

  @Subscribe
  public void activatedEnemy(ActivatedEnemy activatedEnemy) {
    markings.activate(activatedEnemy.getObject());
  }

  @Subscribe
  public void moving(Moving moving) {
    for (Terrain terrain : moving.getMovement().getTerrains()) {
      markings.mark(terrain, markerSprites.getMove());
    }
  }

  @Subscribe
  public void selectingTarget(SelectingTarget selectingTarget) {
    for (Target target : selectingTarget.getTargets()) {
      markTarget(target);
    }
  }

  @Subscribe
  public void battling(Battling battling) {
    markTarget(battling.getTarget());
  }

  @Subscribe
  public void exitState(ExitState exitState) {
    markings.clearMarked();
    markings.activate(null);
  }

  // TODO: this is temporary
  @Subscribe
  public void attack(final Attack attack) {
    Runnable showAttack = new Runnable() {
      @Override
      public void run() {
        for (MapObject object : attack.getObject().getHitObjects()) {
          markings.mark(object, markerSprites.getHit());
        }
      }
    };
    Runnable hideAttack = new Runnable() {
      @Override
      public void run() {
        markings.clearMarked();
      }
    };

    SequenceAction sequence = Actions.sequence(
        Actions.run(showAttack),
        Actions.delay(0.25f),
        Actions.run(hideAttack),
        Actions.delay(0.1f),
        Actions.run(showAttack),
        Actions.delay(0.2f),
        Actions.run(hideAttack),
        Actions.run(new Runnable() {
          @Override
          public void run() {
            attack.done();
          }
        }));
    markings.addAction(sequence);
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
