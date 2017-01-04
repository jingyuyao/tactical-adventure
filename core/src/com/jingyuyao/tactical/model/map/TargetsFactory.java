package com.jingyuyao.tactical.model.map;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Table;
import com.google.common.graph.Graph;
import com.jingyuyao.tactical.model.Algorithms;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.item.Weapon;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TargetsFactory {
  private final Characters characters;
  private final Terrains terrains;

  @Inject
  public TargetsFactory(Characters characters, Terrains terrains) {
    this.characters = characters;
    this.terrains = terrains;
  }

  /** Magic. */
  public Targets create(Character character) {
    Graph<Coordinate> moveGraph = getMoveGraph(character);
    SetMultimap<Coordinate, SetMultimap<Coordinate, Weapon>> moveMap = HashMultimap.create();
    for (Coordinate move : moveGraph.nodes()) {
      SetMultimap<Coordinate, Weapon> targetWeaponMap = HashMultimap.create();
      for (Weapon weapon : character.getWeapons()) {
        // TODO: we need to be smarter if we want irregular weapon target areas
        // we also needs a different class of target indicators for user targetable weapons
        for (int distance : weapon.getAttackDistances()) {
          for (Coordinate target :
              Algorithms.getNDistanceAway(
                  terrains.getWidth(), terrains.getHeight(), move, distance)) {
            targetWeaponMap.put(target, weapon);
          }
        }
      }
      moveMap.put(move, targetWeaponMap);
    }
    return new Targets(characters, character, moveGraph, moveMap);
  }

  private Graph<Coordinate> getMoveGraph(Character character) {
    return Algorithms.minPathSearch(
        createMovementPenaltyTable(character),
        character.getCoordinate(),
        character.getMoveDistance());
  }

  private Table<Integer, Integer, Integer> createMovementPenaltyTable(Character character) {
    Table<Integer, Integer, Integer> movementPenaltyTable =
        HashBasedTable.create(terrains.getWidth(), terrains.getHeight());

    for (Terrain terrain : terrains) {
      Coordinate coordinate = terrain.getCoordinate();
      movementPenaltyTable.put(
          coordinate.getX(), coordinate.getY(), terrain.getMovementPenalty(character));
    }

    for (Character blocked : characters) {
      Coordinate coordinate = blocked.getCoordinate();
      movementPenaltyTable.put(coordinate.getX(), coordinate.getY(), Algorithms.NO_EDGE);
    }

    return movementPenaltyTable;
  }
}
