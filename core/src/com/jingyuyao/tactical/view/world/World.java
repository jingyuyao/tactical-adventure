package com.jingyuyao.tactical.view.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.controller.ControllerFactory;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.AddEnemy;
import com.jingyuyao.tactical.model.event.AddPlayer;
import com.jingyuyao.tactical.model.event.AddTerrain;
import com.jingyuyao.tactical.model.event.RemoveObject;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.view.actor.ActorFactory;
import com.jingyuyao.tactical.view.actor.WorldActor;
import com.jingyuyao.tactical.view.world.WorldModule.BackingActorMap;
import com.jingyuyao.tactical.view.world.WorldModule.CharacterGroup;
import com.jingyuyao.tactical.view.world.WorldModule.CharacterSprites;
import com.jingyuyao.tactical.view.world.WorldModule.TerrainGroup;
import com.jingyuyao.tactical.view.world.WorldModule.WorldStage;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Contains and renders the stage. The stage is rendered in a grid scale (i.e. showing a 30x20
 * grid).
 */
@Singleton
public class World {

  private final Stage stage;
  private final Group characterGroup;
  private final Group terrainGroup;
  private final OrthogonalTiledMapRenderer mapRenderer;
  private final ActorFactory actorFactory;
  private final ControllerFactory controllerFactory;
  private final Map<String, Sprite> nameSpriteMap;
  private final Map<MapObject, WorldActor<?>> actorMap;

  /**
   * A map view contains a stage with all the actors and a way to render them. The background map is
   * backed by a {@link OrthogonalTiledMapRenderer}.
   */
  @Inject
  World(
      @WorldStage Stage stage,
      @CharacterGroup Group characterGroup,
      @TerrainGroup Group terrainGroup,
      OrthogonalTiledMapRenderer mapRenderer,
      ActorFactory actorFactory,
      ControllerFactory controllerFactory,
      @CharacterSprites Map<String, Sprite> nameSpriteMap,
      @BackingActorMap Map<MapObject, WorldActor<?>> actorMap) {
    this.stage = stage;
    this.characterGroup = characterGroup;
    this.terrainGroup = terrainGroup;
    this.mapRenderer = mapRenderer;
    this.actorFactory = actorFactory;
    this.controllerFactory = controllerFactory;
    this.nameSpriteMap = nameSpriteMap;
    this.actorMap = actorMap;
  }

  @Subscribe
  public void addTerrain(AddTerrain addTerrain) {
    Terrain terrain = addTerrain.getObject();
    addActor(terrain, actorFactory.create(terrain), terrainGroup);
  }

  @Subscribe
  public void addPlayer(AddPlayer addPlayer) {
    Player player = addPlayer.getObject();
    Sprite sprite = nameSpriteMap.get(player.getName());
    addActor(player, actorFactory.create(player, sprite), characterGroup);
  }

  @Subscribe
  public void addEnemy(AddEnemy addEnemy) {
    Enemy enemy = addEnemy.getObject();
    Sprite sprite = nameSpriteMap.get(enemy.getName());
    addActor(enemy, actorFactory.create(enemy, sprite), characterGroup);
  }

  @Subscribe
  public void removeObject(RemoveObject removeObject) {
    WorldActor actor = actorMap.remove(removeObject.getObject());
    Preconditions.checkNotNull(actor);
    actor.remove();
  }

  public WorldActor get(MapObject object) {
    return actorMap.get(object);
  }

  public void act(float delta) {
    stage.act(delta);
  }

  public void draw() {
    stage.getViewport().apply();
    mapRenderer.setView((OrthographicCamera) stage.getCamera());
    mapRenderer.render();
    stage.draw();
  }

  public void resize(int width, int height) {
    // TODO: update camera so we don't show black bars
    stage.getViewport().update(width, height);
  }

  public void dispose() {
    stage.dispose();
  }

  private void addActor(MapObject object, WorldActor<?> actor, Group group) {
    actor.addListener(controllerFactory.create(object));
    group.addActor(actor);
    actorMap.put(object, actor);
  }
}
