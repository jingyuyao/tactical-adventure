package com.jingyuyao.tactical.model.map;

import com.google.common.graph.Graph;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Algorithms;
import com.jingyuyao.tactical.model.common.Coordinate;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MovementFactory {

  private final Algorithms algorithms;
  private final Terrains terrains;

  @Inject
  public MovementFactory(Algorithms algorithms, Terrains terrains) {
    this.algorithms = algorithms;
    this.terrains = terrains;
  }

  public Movement create(Character character) {
    return new Movement(algorithms, terrains, createMoveGraph(character));
  }

  private Graph<Coordinate> createMoveGraph(Character character) {
    return algorithms.distanceFromGraph(
        character.createMovementPenaltyFunction(),
        character.getCoordinate(),
        character.getMoveDistance());
  }
}
