package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.event.Attack;
import com.jingyuyao.tactical.model.event.HideMovement;
import com.jingyuyao.tactical.model.event.HideTarget;
import com.jingyuyao.tactical.model.event.ShowMovement;
import com.jingyuyao.tactical.model.event.ShowTarget;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Movement;
import com.jingyuyao.tactical.model.state.MapState;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.view.ViewModule.MapMarkingActor;
import com.jingyuyao.tactical.view.actor.MapActor;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class MapMarkings {

  private final Actor actor;
  private final Batch batch;
  private final Map<MapObject, MapActor<?>> actorMap;
  private final Map<Marker, Sprite> markerSpriteMap;
  private final MapState mapState;

  @Inject
  MapMarkings(
      @MapMarkingActor Actor actor,
      Batch batch,
      Map<MapObject, MapActor<?>> actorMap,
      Map<Marker, Sprite> markerSpriteMap,
      MapState mapState) {
    this.actor = actor;
    this.batch = batch;
    this.actorMap = actorMap;
    this.markerSpriteMap = markerSpriteMap;
    this.mapState = mapState;
  }

  @Subscribe
  public void showMovement(ShowMovement showMovement) {
    Movement movement = showMovement.getObject();
    for (Terrain terrain : movement.getTerrains()) {
      addMarker(terrain, Marker.CAN_MOVE_TO);
    }
  }

  @Subscribe
  public void hideMovement(HideMovement hideMovement) {
    Movement movement = hideMovement.getObject();
    for (Terrain terrain : movement.getTerrains()) {
      removeMarker(terrain, Marker.CAN_MOVE_TO);
    }
  }

  @Subscribe
  public void showTarget(ShowTarget showTarget) {
    Target target = showTarget.getObject();
    for (Terrain terrain : target.getSelectTerrains()) {
      addMarker(terrain, Marker.TARGET_SELECT);
    }
    for (Terrain terrain : target.getTargetTerrains()) {
      addMarker(terrain, Marker.CAN_ATTACK);
    }
  }

  @Subscribe
  public void hideTarget(HideTarget hideTarget) {
    Target target = hideTarget.getObject();
    for (Terrain terrain : target.getSelectTerrains()) {
      removeMarker(terrain, Marker.TARGET_SELECT);
    }
    for (Terrain terrain : target.getTargetTerrains()) {
      removeMarker(terrain, Marker.CAN_ATTACK);
    }
  }

  @Subscribe
  public void attack(final Attack attack) {
    Runnable show = new Runnable() {
      @Override
      public void run() {
        for (MapObject object : attack.getObject().getHitObjects()) {
          addMarker(object, Marker.HIT);
        }
      }
    };
    Runnable hide = new Runnable() {
      @Override
      public void run() {
        for (MapObject object : attack.getObject().getHitObjects()) {
          removeMarker(object, Marker.HIT);
        }
      }
    };

    SequenceAction sequence = Actions.sequence(
        Actions.run(show),
        Actions.delay(0.25f),
        Actions.run(hide),
        Actions.delay(0.1f),
        Actions.run(show),
        Actions.delay(0.2f),
        Actions.run(hide),
        Actions.run(
            new Runnable() {
              @Override
              public void run() {
                attack.done();
              }
            }));
    actor.addAction(sequence);
  }

  void act(float delta) {
    actor.act(delta);
  }

  void draw() {
    Optional<MapObject> highlightOptional = mapState.getCurrentHighlight();
    if (highlightOptional.isPresent()) {
      MapActor actor = actorMap.get(highlightOptional.get());
      Sprite highlightSprite = markerSpriteMap.get(Marker.HIGHLIGHT);
      highlightSprite.setBounds(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
      batch.begin();
      highlightSprite.draw(batch);
      batch.end();
    }
  }

  private void addMarker(MapObject object, Marker marker) {
    actorMap.get(object).addMarkerSprite(markerSpriteMap.get(marker));
  }

  private void removeMarker(MapObject object, Marker marker) {
    actorMap.get(object).removeMarkerSprite(markerSpriteMap.get(marker));
  }
}
