package com.jingyuyao.tactical.view.actor;

import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.view.actor.ActorModule.InitialMarkers;
import com.jingyuyao.tactical.view.resource.LoopAnimation;
import com.jingyuyao.tactical.view.resource.WorldTexture;
import java.util.LinkedHashSet;
import javax.inject.Inject;
import javax.inject.Provider;

public class ActorFactory {

  private final ActorConfig actorConfig;
  private final Provider<LinkedHashSet<WorldTexture>> markersProvider;

  @Inject
  ActorFactory(
      ActorConfig actorConfig,
      @InitialMarkers Provider<LinkedHashSet<WorldTexture>> markersProvider) {
    this.actorConfig = actorConfig;
    this.markersProvider = markersProvider;
  }

  public CellActor create(Cell cell, Coordinate initialCoordinate) {
    return new CellActor(cell, initialCoordinate, actorConfig, markersProvider.get());
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
}
