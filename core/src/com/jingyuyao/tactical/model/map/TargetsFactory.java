package com.jingyuyao.tactical.model.map;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.graph.Graph;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Algorithms;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Weapon;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TargetsFactory {

  private final Algorithms algorithms;
  private final Characters characters;
  private final Terrains terrains;

  @Inject
  public TargetsFactory(Algorithms algorithms, Characters characters, Terrains terrains) {
    this.algorithms = algorithms;
    this.characters = characters;
    this.terrains = terrains;
  }

  /**
   * Magic.
   */
  public Targets create(Character character) {
    Graph<Coordinate> moveGraph = getMoveGraph(character);
    Map<Coordinate, SetMultimap<Coordinate, Weapon>> moveMap =
        new HashMap<Coordinate, SetMultimap<Coordinate, Weapon>>();
    for (Coordinate move : moveGraph.nodes()) {
      SetMultimap<Coordinate, Weapon> targetWeaponMap = HashMultimap.create();
      for (Weapon weapon : character.getWeapons()) {
        // TODO: we need to be smarter if we want irregular weapon target areas
        // we also needs a different class of target indicators for user targetable weapons
        for (int distance : weapon.getAttackDistances()) {
          for (Coordinate target :
              algorithms.getNDistanceAway(
                  terrains.getWidth(), terrains.getHeight(), move, distance)) {
            targetWeaponMap.put(target, weapon);
          }
        }
      }
      moveMap.put(move, targetWeaponMap);
    }
    return new Targets(algorithms, characters, character, moveGraph, moveMap);
  }

  private Graph<Coordinate> getMoveGraph(Character character) {
    return algorithms.minPathSearch(
        terrains.getWidth(),
        terrains.getHeight(),
        createMovementPenaltyMap(character),
        character.getCoordinate(),
        character.getMoveDistance());
  }

  private Map<Coordinate, Integer> createMovementPenaltyMap(Character character) {
    Map<Coordinate, Integer> penaltyMap =
        new HashMap<Coordinate, Integer>(terrains.getWidth() * terrains.getHeight());

    for (Terrain terrain : terrains) {
      Coordinate coordinate = terrain.getCoordinate();
      penaltyMap.put(coordinate, terrain.getMovementPenalty(character));
    }

    for (Character blocked : characters) {
      Coordinate coordinate = blocked.getCoordinate();
      penaltyMap.put(coordinate, Algorithms.NO_EDGE);
    }

    return penaltyMap;
  }
}
