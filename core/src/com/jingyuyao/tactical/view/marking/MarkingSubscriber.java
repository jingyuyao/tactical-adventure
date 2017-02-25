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
public class MarkingSubscriber {

  private final WorldMarkings worldMarkings;
  private final MarkerSprites markerSprites;

  @Inject
  MarkingSubscriber(WorldMarkings worldMarkings, MarkerSprites markerSprites) {
    this.worldMarkings = worldMarkings;
    this.markerSprites = markerSprites;
  }

  @Subscribe
  public void selectObject(SelectObject<MapObject> selectObject) {
    worldMarkings.highlight(selectObject.getObject());
  }

  @Subscribe
  public void playerState(PlayerState playerState) {
    worldMarkings.activate(playerState.getPlayer());
  }

  @Subscribe
  public void activatedEnemy(ActivatedEnemy activatedEnemy) {
    worldMarkings.activate(activatedEnemy.getObject());
  }

  @Subscribe
  public void moving(Moving moving) {
    for (Terrain terrain : moving.getMovement().getTerrains()) {
      worldMarkings.mark(terrain, markerSprites.getMove());
    }
  }

  @Subscribe
  public void selectingTarget(SelectingTarget selectingTarget) {
    for (Target target : selectingTarget.getTargets()) {
      for (Terrain terrain : target.getTargetTerrains()) {
        worldMarkings.mark(terrain, markerSprites.getAttack());
      }
      for (Terrain terrain : target.getSelectTerrains()) {
        worldMarkings.mark(terrain, markerSprites.getTargetSelect());
      }
    }
  }

  @Subscribe
  public void battling(Battling battling) {
    for (Terrain terrain : battling.getTarget().getTargetTerrains()) {
      worldMarkings.mark(terrain, markerSprites.getAttack());
    }
    for (Terrain terrain : battling.getTarget().getSelectTerrains()) {
      worldMarkings.mark(terrain, markerSprites.getTargetSelect());
    }
  }

  @Subscribe
  public void exitState(ExitState exitState) {
    worldMarkings.clearMarked();
    worldMarkings.activate(null);
  }

  // TODO: this is temporary
  @Subscribe
  public void attack(final Attack attack) {
    Runnable showAttack = new Runnable() {
      @Override
      public void run() {
        for (MapObject object : attack.getObject().getHitObjects()) {
          worldMarkings.mark(object, markerSprites.getHit());
        }
      }
    };
    Runnable hideAttack = new Runnable() {
      @Override
      public void run() {
        worldMarkings.clearMarked();
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
    worldMarkings.addAction(sequence);
  }
}
