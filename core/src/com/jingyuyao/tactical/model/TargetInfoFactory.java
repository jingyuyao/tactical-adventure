package com.jingyuyao.tactical.model;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.graph.Graph;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.object.Character;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TargetInfoFactory {
    private final Map map;
    private final TerrainGrid terrainGrid;

    @Inject
    public TargetInfoFactory(Map map, TerrainGrid terrainGrid) {
        this.map = map;
        this.terrainGrid = terrainGrid;
    }

    /**
     * Magic.
     */
    public TargetInfo create(Character character) {
        Graph<Coordinate> moveGraph = map.getMoveGraph(character);
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
        return new TargetInfo(map, character, moveGraph, moveMap);
    }
}
