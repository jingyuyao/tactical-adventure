package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.google.common.collect.Multimap;
import com.google.common.eventbus.Subscribe;
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
import com.jingyuyao.tactical.view.ViewModule.ActivatedCharacterSprite;
import com.jingyuyao.tactical.view.ViewModule.HighlightSprite;
import com.jingyuyao.tactical.view.ViewModule.MapMarkingsActionActor;
import com.jingyuyao.tactical.view.actor.MapActor;
import com.jingyuyao.tactical.view.marking.MarkingFactory;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class MapMarkings {

  private final Actor actionActor;
  private final Batch batch;
  private final Map<MapObject, MapActor<?>> actorMap;
  private final List<MapActor<?>> markedActorList;
  private final MarkingFactory markingFactory;
  private final Sprite highlightSprite;
  private final Sprite activatedCharacterSprite;
  private MapActor selectedActor;
  private MapActor activatedActor;

  @Inject
  MapMarkings(
      @MapMarkingsActionActor Actor actionActor,
      Batch batch,
      Map<MapObject, MapActor<?>> actorMap,
      List<MapActor<?>> markedActorList,
      MarkingFactory markingFactory,
      @HighlightSprite Sprite highlightSprite,
      @ActivatedCharacterSprite Sprite activatedCharacterSprite) {
    this.actionActor = actionActor;
    this.batch = batch;
    this.actorMap = actorMap;
    this.markedActorList = markedActorList;
    this.markingFactory = markingFactory;
    this.highlightSprite = highlightSprite;
    this.activatedCharacterSprite = activatedCharacterSprite;
  }

  @Subscribe
  public void selectPlayer(SelectPlayer selectPlayer) {
    selectedActor = actorMap.get(selectPlayer.getObject());
  }

  @Subscribe
  public void selectEnemy(SelectEnemy selectEnemy) {
    selectedActor = actorMap.get(selectEnemy.getObject());
  }

  @Subscribe
  public void selectTerrain(SelectTerrain selectTerrain) {
    selectedActor = actorMap.get(selectTerrain.getObject());
  }

  @Subscribe
  public void playerState(PlayerState playerState) {
    activatedActor = actorMap.get(playerState.getPlayer());
  }

  @Subscribe
  public void moving(Moving moving) {
    show(markingFactory.createMovement(moving.getMovement()));
  }

  @Subscribe
  public void selectingTarget(SelectingTarget selectingTarget) {
    for (Target target : selectingTarget.getTargets()) {
      show(markingFactory.createTarget(target));
    }
  }

  @Subscribe
  public void battling(Battling battling) {
    show(markingFactory.createTarget(battling.getTarget()));
  }

  @Subscribe
  public void activatedEnemy(ActivatedEnemy activatedEnemy) {
    activatedActor = actorMap.get(activatedEnemy.getObject());
  }

  @Subscribe
  public void exitState(ExitState exitState) {
    for (MapActor actor : markedActorList) {
      actor.clearMarkerSprites();
    }
    markedActorList.clear();
    activatedActor = null;
  }

  @Subscribe
  public void attack(final Attack attack) {
    final Multimap<MapObject, Sprite> hitMap = markingFactory.createHit(attack.getObject());

    Runnable showAttack = new Runnable() {
      @Override
      public void run() {
        show(hitMap);
      }
    };
    Runnable hideAttack = new Runnable() {
      @Override
      public void run() {
        hide(hitMap);
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

  void act(float delta) {
    actionActor.act(delta);
  }

  void draw() {
    batch.begin();
    if (selectedActor != null) {
      highlightSprite.setBounds(
          selectedActor.getX(), selectedActor.getY(), selectedActor.getWidth(),
          selectedActor.getHeight());
      highlightSprite.draw(batch);
    }
    if (activatedActor != null) {
      activatedCharacterSprite.setBounds(
          activatedActor.getX(), activatedActor.getY(), activatedActor.getWidth(),
          activatedActor.getHeight());
      activatedCharacterSprite.draw(batch);
    }
    batch.end();
  }

  private void show(Multimap<MapObject, Sprite> multimap) {
    for (Entry<MapObject, Sprite> entry : multimap.entries()) {
      MapActor actor = actorMap.get(entry.getKey());
      actor.addMarkerSprite(entry.getValue());
      markedActorList.add(actor);
    }
  }

  private void hide(Multimap<MapObject, Sprite> multimap) {
    for (Entry<MapObject, Sprite> entry : multimap.entries()) {
      MapActor actor = actorMap.get(entry.getKey());
      actor.removeMarkerSprite(entry.getValue());
      markedActorList.remove(actor);
    }
  }
}
