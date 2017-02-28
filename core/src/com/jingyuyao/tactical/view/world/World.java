package com.jingyuyao.tactical.view.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.controller.ControllerFactory;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.view.actor.ActorFactory;
import com.jingyuyao.tactical.view.actor.EnemyActor;
import com.jingyuyao.tactical.view.actor.PlayerActor;
import com.jingyuyao.tactical.view.actor.TerrainActor;
import com.jingyuyao.tactical.view.actor.WorldActor;
import com.jingyuyao.tactical.view.resource.Animations;
import com.jingyuyao.tactical.view.resource.LoopAnimation;
import com.jingyuyao.tactical.view.world.WorldModule.BackingActorMap;
import com.jingyuyao.tactical.view.world.WorldModule.CharacterGroup;
import com.jingyuyao.tactical.view.world.WorldModule.TerrainGroup;
import com.jingyuyao.tactical.view.world.WorldModule.WorldStage;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class World {

  private final Stage stage;
  private final Group characterGroup;
  private final Group terrainGroup;
  private final Map<MapObject, WorldActor<?>> actorMap;
  private final OrthogonalTiledMapRenderer mapRenderer;
  private final ActorFactory actorFactory;
  private final ControllerFactory controllerFactory;
  private final Animations animations;

  @Inject
  World(
      @WorldStage Stage stage,
      @CharacterGroup Group characterGroup,
      @TerrainGroup Group terrainGroup,
      @BackingActorMap Map<MapObject, WorldActor<?>> actorMap,
      OrthogonalTiledMapRenderer mapRenderer,
      ActorFactory actorFactory,
      ControllerFactory controllerFactory,
      Animations animations) {
    this.stage = stage;
    this.characterGroup = characterGroup;
    this.terrainGroup = terrainGroup;
    this.actorMap = actorMap;
    this.mapRenderer = mapRenderer;
    this.actorFactory = actorFactory;
    this.controllerFactory = controllerFactory;
    this.animations = animations;
    stage.addActor(terrainGroup);
    stage.addActor(characterGroup);
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

  public WorldActor<?> get(MapObject object) {
    return actorMap.get(object);
  }

  void add(Player player) {
    PlayerActor actor = actorFactory.create(player, getAnimation(player.getName()));
    addActor(player, actor, characterGroup);
  }

  void add(Enemy enemy) {
    EnemyActor actor = actorFactory.create(enemy, getAnimation(enemy.getName()));
    addActor(enemy, actor, characterGroup);
  }

  void add(Terrain terrain) {
    TerrainActor actor = actorFactory.create(terrain);
    addActor(terrain, actor, terrainGroup);
  }

  void remove(MapObject object) {
    Preconditions.checkArgument(actorMap.containsKey(object));
    WorldActor actor = actorMap.remove(object);
    actor.remove();
  }

  private void addActor(MapObject object, WorldActor<?> actor, Group group) {
    actor.addListener(controllerFactory.create(object));
    group.addActor(actor);
    actorMap.put(object, actor);
  }

  private LoopAnimation getAnimation(String characterName) {
    return animations.getCharacter(characterName);
  }
}
