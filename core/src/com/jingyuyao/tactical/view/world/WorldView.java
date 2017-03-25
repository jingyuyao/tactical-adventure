package com.jingyuyao.tactical.view.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.view.actor.ActorFactory;
import com.jingyuyao.tactical.view.actor.EnemyActor;
import com.jingyuyao.tactical.view.actor.PlayerActor;
import com.jingyuyao.tactical.view.actor.TerrainActor;
import com.jingyuyao.tactical.view.actor.WorldActor;
import com.jingyuyao.tactical.view.resource.Animations;
import com.jingyuyao.tactical.view.resource.LoopAnimation;
import com.jingyuyao.tactical.view.world.WorldModule.BackingActorMap;
import com.jingyuyao.tactical.view.world.WorldModule.CellGroup;
import com.jingyuyao.tactical.view.world.WorldModule.CharacterGroup;
import com.jingyuyao.tactical.view.world.WorldModule.TerrainGroup;
import com.jingyuyao.tactical.view.world.WorldModule.WorldStage;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldView {

  private final Stage stage;
  private final Group cellGroup;
  private final Group characterGroup;
  private final Group terrainGroup;
  private final Map<Object, WorldActor<?>> actorMap;
  private final OrthogonalTiledMapRenderer mapRenderer;
  private final ActorFactory actorFactory;
  private final Animations animations;

  @Inject
  WorldView(
      @WorldStage Stage stage,
      @CellGroup Group cellGroup,
      @CharacterGroup Group characterGroup,
      @TerrainGroup Group terrainGroup,
      @BackingActorMap Map<Object, WorldActor<?>> actorMap,
      OrthogonalTiledMapRenderer mapRenderer,
      ActorFactory actorFactory,
      Animations animations) {
    this.stage = stage;
    this.cellGroup = cellGroup;
    this.characterGroup = characterGroup;
    this.terrainGroup = terrainGroup;
    this.actorMap = actorMap;
    this.mapRenderer = mapRenderer;
    this.actorFactory = actorFactory;
    this.animations = animations;
    stage.addActor(terrainGroup);
    stage.addActor(characterGroup);
    stage.addActor(cellGroup);
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

  public void reset() {
    cellGroup.clear();
    characterGroup.clear();
    terrainGroup.clear();
  }

  public WorldActor<?> get(Object object) {
    return actorMap.get(object);
  }

  void add(Cell cell) {
    Coordinate coordinate = cell.getCoordinate();
    if (cell.hasPlayer()) {
      add(coordinate, cell.getPlayer());
    } else if (cell.hasEnemy()) {
      add(coordinate, cell.getEnemy());
    }
    add(coordinate, cell.getTerrain());
    cellGroup.addActor(actorFactory.create(cell));
  }

  void add(Coordinate coordinate, Player player) {
    PlayerActor actor = actorFactory.create(player, coordinate, getAnimation(player));
    addActor(player, actor, characterGroup);
  }

  void add(Coordinate coordinate, Enemy enemy) {
    EnemyActor actor = actorFactory.create(enemy, coordinate, getAnimation(enemy));
    addActor(enemy, actor, characterGroup);
  }

  void add(Coordinate coordinate, Terrain terrain) {
    TerrainActor actor = actorFactory.create(terrain, coordinate);
    addActor(terrain, actor, terrainGroup);
  }

  void remove(Object object) {
    Preconditions.checkArgument(actorMap.containsKey(object));
    WorldActor actor = actorMap.remove(object);
    actor.remove();
  }

  private void addActor(Object object, WorldActor<?> actor, Group group) {
    group.addActor(actor);
    actorMap.put(object, actor);
  }

  private LoopAnimation getAnimation(Character character) {
    return animations.getCharacter(character.getName());
  }
}
