package com.jingyuyao.tactical.model.map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.graph.Graph;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Algorithms;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.mark.MarkingFactory;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MovementFactory {

  private final Algorithms algorithms;
  private final Characters characters;
  private final Terrains terrains;
  private final MarkingFactory markingFactory;

  @Inject
  public MovementFactory(
      Algorithms algorithms,
      Characters characters,
      Terrains terrains,
      MarkingFactory markingFactory) {
    this.algorithms = algorithms;
    this.characters = characters;
    this.terrains = terrains;
    this.markingFactory = markingFactory;
  }

  public Movement create(Character character) {
    return new Movement(algorithms, terrains, createMoveGraph(character), markingFactory);
  }

  private Graph<Coordinate> createMoveGraph(Character character) {
    return algorithms.minPathSearch(
        terrains.getWidth(),
        terrains.getHeight(),
        createMovementPenaltyFunction(character),
        character.getCoordinate(),
        character.getMoveDistance());
  }

  private Function<Coordinate, Integer> createMovementPenaltyFunction(final Character character) {
    final ImmutableSet<Coordinate> blockedCoordinates = characters.coordinates();
    return new Function<Coordinate, Integer>() {
      @Override
      public Integer apply(Coordinate input) {
        if (blockedCoordinates.contains(input)) {
          return Algorithms.NO_EDGE;
        }
        Terrain terrain = terrains.get(input);
        if (terrain == null) {
          return Algorithms.NO_EDGE;
        }
        return terrain.getMovementPenalty(character);
      }
    };
  }
}
