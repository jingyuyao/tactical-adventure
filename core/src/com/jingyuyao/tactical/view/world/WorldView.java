package com.jingyuyao.tactical.view.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.view.actor.ActorFactory;
import com.jingyuyao.tactical.view.actor.CharacterActor;
import com.jingyuyao.tactical.view.actor.WorldActor;
import com.jingyuyao.tactical.view.world.WorldModule.WorldStage;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldView {

  private final Stage stage;
  private final MapGroup<Cell, Actor> cellGroup;
  private final MapGroup<Character, CharacterActor> characterGroup;
  private final MapGroup<Terrain, WorldActor> terrainGroup;
  private final OrthogonalTiledMapRenderer mapRenderer;
  private final ActorFactory actorFactory;

  @Inject
  WorldView(
      @WorldStage Stage stage,
      MapGroup<Cell, Actor> cellGroup,
      MapGroup<Character, CharacterActor> characterGroup,
      MapGroup<Terrain, WorldActor> terrainGroup,
      OrthogonalTiledMapRenderer mapRenderer,
      ActorFactory actorFactory) {
    this.stage = stage;
    this.cellGroup = cellGroup;
    this.characterGroup = characterGroup;
    this.terrainGroup = terrainGroup;
    this.mapRenderer = mapRenderer;
    this.actorFactory = actorFactory;
    terrainGroup.addToStage(stage);
    characterGroup.addToStage(stage);
    cellGroup.addToStage(stage);
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

  public void reset() {
    cellGroup.clear();
    characterGroup.clear();
    terrainGroup.clear();
  }

  public void dispose() {
    stage.dispose();
  }

  public Actor get(Cell cell) {
    return cellGroup.get(cell);
  }

  public WorldActor get(Terrain terrain) {
    return terrainGroup.get(terrain);
  }

  public CharacterActor get(Character character) {
    return characterGroup.get(character);
  }

  void add(Cell cell) {
    cellGroup.add(cell, actorFactory.create(cell));
  }

  void add(Coordinate coordinate, Terrain terrain) {
    terrainGroup.add(terrain, actorFactory.create(coordinate));
  }

  void add(Coordinate coordinate, Player player) {
    characterGroup.add(player, actorFactory.create(player, coordinate));
  }

  void add(Coordinate coordinate, Enemy enemy) {
    characterGroup.add(enemy, actorFactory.create(enemy, coordinate));
  }

  void remove(Character character) {
    characterGroup.remove(character);
  }
}
