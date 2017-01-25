package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.controller.InputLock;
import com.jingyuyao.tactical.model.common.EventSubscriber;
import com.jingyuyao.tactical.model.item.event.Attack;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.view.ViewAnnotations.MapViewStage;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AttackSubscriber implements EventSubscriber {

  private final Stage mapStage;
  private final Actors actors;
  private final InputLock inputLock;
  private final Map<Marker, Sprite> markerSpriteMap;

  @Inject
  AttackSubscriber(
      @MapViewStage Stage mapStage,
      Actors actors,
      InputLock inputLock,
      Map<Marker, Sprite> markerSpriteMap) {
    this.mapStage = mapStage;
    this.actors = actors;
    this.inputLock = inputLock;
    this.markerSpriteMap = markerSpriteMap;
  }

  @Subscribe
  public void attacked(final Attack attack) {
    inputLock.lock();

    final Iterable<MapActor> targets = actors.getAll(attack.getObject().getAllTargetObjects());

    Runnable showHitMarker = new Runnable() {
      @Override
      public void run() {
        for (MapActor target : targets) {
          target.addMarkerSprite(markerSpriteMap.get(Marker.DANGER));
        }
      }
    };
    Runnable hideHitMarker = new Runnable() {
      @Override
      public void run() {
        for (MapActor target : targets) {
          target.removeMarkerSprite(markerSpriteMap.get(Marker.DANGER));
        }
      }
    };

    SequenceAction sequence = Actions.sequence(
        Actions.run(showHitMarker),
        Actions.delay(0.25f),
        Actions.run(hideHitMarker),
        Actions.delay(0.1f),
        Actions.run(showHitMarker),
        Actions.delay(0.2f),
        Actions.run(hideHitMarker),
        Actions.run(
            new Runnable() {
              @Override
              public void run() {
                inputLock.unlock();
                attack.done();
              }
            }));
    // Attach action to stage instead to a particular action since the attacker could have died
    mapStage.addAction(sequence);
  }
}
