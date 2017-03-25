package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.jingyuyao.tactical.controller.ControllerFactory;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.view.actor.ActorModule.InitialMarkers;
import com.jingyuyao.tactical.view.resource.Animations;
import com.jingyuyao.tactical.view.resource.LoopAnimation;
import com.jingyuyao.tactical.view.resource.WorldTexture;
import java.util.LinkedHashSet;
import javax.inject.Inject;
import javax.inject.Provider;

public class ActorFactory {

  private final ActorConfig actorConfig;
  private final ControllerFactory controllerFactory;
  private final Animations animations;
  private final Provider<LinkedHashSet<WorldTexture>> markersProvider;

  @Inject
  ActorFactory(
      ActorConfig actorConfig,
      ControllerFactory controllerFactory,
      Animations animations,
      @InitialMarkers Provider<LinkedHashSet<WorldTexture>> markersProvider) {
    this.actorConfig = actorConfig;
    this.controllerFactory = controllerFactory;
    this.animations = animations;
    this.markersProvider = markersProvider;
  }

  public Actor create(Cell cell) {
    Actor actor = new Actor();
    setSizeAndPosition(cell.getCoordinate(), actor);
    actor.addListener(controllerFactory.create(cell));
    return actor;
  }

  public TerrainActor create(Terrain terrain, Coordinate initialCoordinate) {
    return new TerrainActor(terrain, initialCoordinate, actorConfig, markersProvider.get());
  }

  public PlayerActor create(
      Player player, Coordinate initialCoordinate, LoopAnimation loopAnimation) {
    return new PlayerActor(
        player, initialCoordinate, actorConfig, markersProvider.get(), loopAnimation);
  }

  public EnemyActor create(Enemy enemy, Coordinate initialCoordinate, LoopAnimation loopAnimation) {
    return new EnemyActor(
        enemy, initialCoordinate, actorConfig, markersProvider.get(), loopAnimation);
  }

  private void setSizeAndPosition(Coordinate coordinate, Actor actor) {
    float size = actorConfig.getActorWorldSize();
    actor.setSize(size, size);
    actor.setPosition(coordinate.getX() * size, coordinate.getY() * size);
  }
}
