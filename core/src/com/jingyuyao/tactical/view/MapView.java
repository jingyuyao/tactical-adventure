package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.controller.ControllerFactory;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.character.event.Attack;
import com.jingyuyao.tactical.model.event.AddEnemy;
import com.jingyuyao.tactical.model.event.AddPlayer;
import com.jingyuyao.tactical.model.event.AddTerrain;
import com.jingyuyao.tactical.model.event.HideMovement;
import com.jingyuyao.tactical.model.event.HideTarget;
import com.jingyuyao.tactical.model.event.RemoveObject;
import com.jingyuyao.tactical.model.event.ShowMovement;
import com.jingyuyao.tactical.model.event.ShowTarget;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Marker;
import com.jingyuyao.tactical.model.map.Movement;
import com.jingyuyao.tactical.model.state.MapState;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.view.ViewModule.MapViewStage;
import com.jingyuyao.tactical.view.actor.ActorFactory;
import com.jingyuyao.tactical.view.actor.MapActor;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Contains and renders the stage. The stage is rendered in a grid scale (i.e. showing a 30x20
 * grid).
 */
@Singleton
public class MapView {

  private final Stage stage;
  private final OrthogonalTiledMapRenderer mapRenderer;
  private final ActorFactory actorFactory;
  private final ControllerFactory controllerFactory;
  private final Map<Marker, Sprite> markerSpriteMap;
  private final Map<String, Sprite> nameSpriteMap;
  private final Map<MapObject, MapActor<?>> actorMap;
  private final MapState mapState;

  /**
   * A map view contains a stage with all the actors and a way to render them. The background map is
   * backed by a {@link OrthogonalTiledMapRenderer}.
   */
  @Inject
  MapView(
      @MapViewStage Stage stage,
      OrthogonalTiledMapRenderer mapRenderer,
      ActorFactory actorFactory,
      ControllerFactory controllerFactory,
      Map<Marker, Sprite> markerSpriteMap,
      Map<String, Sprite> nameSpriteMap,
      Map<MapObject, MapActor<?>> actorMap,
      MapState mapState) {
    this.stage = stage;
    this.mapRenderer = mapRenderer;
    this.actorFactory = actorFactory;
    this.controllerFactory = controllerFactory;
    this.markerSpriteMap = markerSpriteMap;
    this.nameSpriteMap = nameSpriteMap;
    this.actorMap = actorMap;
    this.mapState = mapState;
  }

  @Subscribe
  public void addTerrain(AddTerrain addTerrain) {
    Terrain terrain = addTerrain.getObject();
    addActor(terrain, actorFactory.create(terrain, controllerFactory.create(terrain)));
  }

  @Subscribe
  public void addPlayer(AddPlayer addPlayer) {
    Player player = addPlayer.getObject();
    addActor(
        player,
        actorFactory.create(
            player, controllerFactory.create(player), nameSpriteMap.get(player.getName())));
  }

  @Subscribe
  public void addEnemy(AddEnemy addEnemy) {
    Enemy enemy = addEnemy.getObject();
    addActor(
        enemy,
        actorFactory.create(
            enemy, controllerFactory.create(enemy), nameSpriteMap.get(enemy.getName())));
  }

  @Subscribe
  public void removeObject(RemoveObject removeObject) {
    MapActor actor = actorMap.remove(removeObject.getObject());
    Preconditions.checkNotNull(actor);
    actor.remove();
  }

  @Subscribe
  public void showMovement(ShowMovement showMovement) {
    Movement movement = showMovement.getObject();
    for (Terrain terrain : movement.getTerrains()) {
      actorMap.get(terrain).addMarkerSprite(markerSpriteMap.get(Marker.CAN_MOVE_TO));
    }
  }

  @Subscribe
  public void hideMovement(HideMovement hideMovement) {
    Movement movement = hideMovement.getObject();
    for (Terrain terrain : movement.getTerrains()) {
      actorMap.get(terrain).removeMarkerSprite(markerSpriteMap.get(Marker.CAN_MOVE_TO));
    }
  }

  @Subscribe
  public void showTarget(ShowTarget showTarget) {
    Target target = showTarget.getObject();
    for (Terrain terrain : target.getSelectTerrains()) {
      actorMap.get(terrain).addMarkerSprite(markerSpriteMap.get(Marker.TARGET_SELECT));
    }
    for (Terrain terrain : target.getTargetTerrains()) {
      actorMap.get(terrain).addMarkerSprite(markerSpriteMap.get(Marker.CAN_ATTACK));
    }
  }

  @Subscribe
  public void hideTarget(HideTarget hideTarget) {
    Target target = hideTarget.getObject();
    for (Terrain terrain : target.getSelectTerrains()) {
      actorMap.get(terrain).removeMarkerSprite(markerSpriteMap.get(Marker.TARGET_SELECT));
    }
    for (Terrain terrain : target.getTargetTerrains()) {
      actorMap.get(terrain).removeMarkerSprite(markerSpriteMap.get(Marker.CAN_ATTACK));
    }
  }

  @Subscribe
  public void attack(final Attack attack) {
    Runnable show = new Runnable() {
      @Override
      public void run() {
        showHitMarkers(attack.getObject());
      }
    };
    Runnable hide = new Runnable() {
      @Override
      public void run() {
        hideHitMarkers(attack.getObject());
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
    stage.addAction(sequence);
  }

  void act(float delta) {
    stage.act(delta);
  }

  void draw() {
    stage.getViewport().apply();
    mapRenderer.setView((OrthographicCamera) stage.getCamera());
    mapRenderer.render();
    stage.draw();
    Optional<MapObject> highlightOptional = mapState.getCurrentHighlight();
    if (highlightOptional.isPresent()) {
      MapActor actor = actorMap.get(highlightOptional.get());
      Sprite highlightSprite = markerSpriteMap.get(Marker.HIGHLIGHT);
      highlightSprite.setBounds(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
      stage.getBatch().begin();
      highlightSprite.draw(stage.getBatch());
      stage.getBatch().end();
    }
  }

  void resize(int width, int height) {
    // TODO: update camera so we don't show black bars
    stage.getViewport().update(width, height);
  }

  void dispose() {
    stage.dispose();
  }

  private void addActor(MapObject object, MapActor<?> actor) {
    stage.addActor(actor);
    actorMap.put(object, actor);
  }

  private void showHitMarkers(Target target) {
    for (MapObject object : target.getHitObjects()) {
      actorMap.get(object).addMarkerSprite(markerSpriteMap.get(Marker.HIT));
    }
  }

  private void hideHitMarkers(Target target) {
    for (MapObject object : target.getHitObjects()) {
      actorMap.get(object).removeMarkerSprite(markerSpriteMap.get(Marker.HIT));
    }
  }
}
