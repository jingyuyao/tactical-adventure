package com.jingyuyao.tactical.model;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Table;
import com.google.common.graph.Graph;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.object.Character;
import com.jingyuyao.tactical.model.object.CharacterContainer;
import com.jingyuyao.tactical.model.object.Terrain;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TargetInfoFactory {
    private final CharacterContainer characters;
    private final TerrainGrid terrainGrid;

    @Inject
    public TargetInfoFactory(CharacterContainer characters, TerrainGrid terrainGrid) {
        this.characters = characters;
        this.terrainGrid = terrainGrid;
    }

    /**
     * Magic.
     */
    public TargetInfo create(Character character) {
        Graph<Coordinate> moveGraph = getMoveGraph(character);
        SetMultimap<Coordinate, SetMultimap<Coordinate, Weapon>> moveMap = HashMultimap.create();
        for (Coordinate move : moveGraph.nodes()) {
            SetMultimap<Coordinate, Weapon> targetWeaponMap = HashMultimap.create();
            for (Weapon weapon : character.getWeapons()) {
                // TODO: we need to be smarter if we want irregular weapon target areas
                // we also needs a different class of target indicators for user targetable weapons
                for (int distance : weapon.getAttackDistances()) {
                    for (Coordinate target : Algorithms.getNDistanceAway(
                            terrainGrid.getWidth(), terrainGrid.getHeight(), move, distance)) {
                        targetWeaponMap.put(target, weapon);
                    }
                }
            }
            moveMap.put(move, targetWeaponMap);
        }
        return new TargetInfo(characters, character, moveGraph, moveMap);
    }

    private Graph<Coordinate> getMoveGraph(Character character) {
        return Algorithms.minPathSearch(
                createMovementPenaltyTable(character),
                character.getCoordinate(),
                character.getMoveDistance());
    }

    private Table<Integer, Integer, Integer> createMovementPenaltyTable(Character character) {
        Table<Integer, Integer, Integer> movementPenaltyTable =
                HashBasedTable.create(terrainGrid.getWidth(), terrainGrid.getHeight());

        for (Terrain terrain : terrainGrid) {
            Coordinate coordinate = terrain.getCoordinate();
            movementPenaltyTable.put(coordinate.getX(), coordinate.getY(), terrain.getMovementPenalty(character));
        }

        for (Character blocked : characters) {
            Coordinate coordinate = blocked.getCoordinate();
            movementPenaltyTable.put(coordinate.getX(), coordinate.getY(), Algorithms.NO_EDGE);
        }

        return movementPenaltyTable;
    }
}
