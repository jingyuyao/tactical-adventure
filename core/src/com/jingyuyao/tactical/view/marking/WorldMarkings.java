package com.jingyuyao.tactical.view.marking;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
import com.jingyuyao.tactical.view.actor.WorldActor;
import com.jingyuyao.tactical.view.marking.MarkingModule.MarkedActors;
import com.jingyuyao.tactical.view.marking.MarkingModule.MarkingsActor;
import com.jingyuyao.tactical.view.util.ViewUtil;
import com.jingyuyao.tactical.view.world.World;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldMarkings {

  private final Batch batch;
  private final World world;
  private final MarkerSprites markerSprites;
  private final Actor actionActor;
  private final List<WorldActor<?>> markedActorList;
  private WorldActor highlightedActor;
  private WorldActor activatedActor;

  @Inject
  WorldMarkings(
      Batch batch,
      World world,
      MarkerSprites markerSprites,
      @MarkingsActor Actor actionActor,
      @MarkedActors List<WorldActor<?>> markedActorList) {
    this.batch = batch;
    this.world = world;
    this.markerSprites = markerSprites;
    this.actionActor = actionActor;
    this.markedActorList = markedActorList;
  }

  @Subscribe
  public void selectObject(SelectObject<MapObject> selectObject) {
    highlightedActor = world.get(selectObject.getObject());
  }

  @Subscribe
  public void playerState(PlayerState playerState) {
    activatedActor = world.get(playerState.getPlayer());
  }

  @Subscribe
  public void activatedEnemy(ActivatedEnemy activatedEnemy) {
    activatedActor = world.get(activatedEnemy.getObject());
  }

  @Subscribe
  public void moving(Moving moving) {
    for (Terrain terrain : moving.getMovement().getTerrains()) {
      mark(terrain, markerSprites.getMove());
    }
  }

  @Subscribe
  public void selectingTarget(SelectingTarget selectingTarget) {
    for (Target target : selectingTarget.getTargets()) {
      for (Terrain terrain : target.getTargetTerrains()) {
        mark(terrain, markerSprites.getAttack());
      }
      for (Terrain terrain : target.getSelectTerrains()) {
        mark(terrain, markerSprites.getTargetSelect());
      }
    }
  }

  @Subscribe
  public void battling(Battling battling) {
    for (Terrain terrain : battling.getTarget().getTargetTerrains()) {
      mark(terrain, markerSprites.getAttack());
    }
    for (Terrain terrain : battling.getTarget().getSelectTerrains()) {
      mark(terrain, markerSprites.getTargetSelect());
    }
  }

  @Subscribe
  public void exitState(ExitState exitState) {
    clearMarked();
    activatedActor = null;
  }

  // TODO: this is temporary
  @Subscribe
  public void attack(final Attack attack) {
    Runnable showAttack = new Runnable() {
      @Override
      public void run() {
        for (MapObject object : attack.getObject().getHitObjects()) {
          mark(object, markerSprites.getHit());
        }
      }
    };
    Runnable hideAttack = new Runnable() {
      @Override
      public void run() {
        clearMarked();
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
    actionActor.addAction(sequence);
  }

  public void act(float delta) {
    actionActor.act(delta);
  }

  public void draw() {
    batch.begin();
    if (highlightedActor != null) {
      ViewUtil.draw(batch, markerSprites.getHighlight(), highlightedActor);
    }
    if (activatedActor != null) {
      ViewUtil.draw(batch, markerSprites.getActivated(), activatedActor);
    }
    batch.end();
  }

  private void mark(MapObject object, Sprite sprite) {
    WorldActor actor = world.get(object);
    actor.addMarker(sprite);
    markedActorList.add(actor);
  }

  private void clearMarked() {
    for (WorldActor actor : markedActorList) {
      actor.clearMarkers();
    }
    markedActorList.clear();
  }
}
