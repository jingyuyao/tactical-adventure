package com.jingyuyao.tactical.model;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Table;
import com.google.common.graph.ValueGraph;
import com.jingyuyao.tactical.model.object.Character;
import com.jingyuyao.tactical.model.object.EnemyContainer;
import com.jingyuyao.tactical.model.object.PlayerContainer;
import com.jingyuyao.tactical.model.object.Terrain;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Map {
    private final TerrainGrid terrains;
    private final PlayerContainer players;
    private final EnemyContainer enemies;

    @Inject
    public Map(TerrainGrid terrains, PlayerContainer players, EnemyContainer enemies) {
        this.terrains = terrains;
        this.players = players;
        this.enemies = enemies;
    }

    public Iterable<Character> getCharacters() {
        return Iterables.concat(players, enemies);
    }

    public ValueGraph<Coordinate, Integer> getMoveGraph(Character character) {
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
            movementPenaltyTable.put(coordinate.getX(), coordinate.getY(), terrain.getMovementPenalty(character));
        }

        for (Character blocked : getCharacters()) {
            Coordinate coordinate = blocked.getCoordinate();
            movementPenaltyTable.put(coordinate.getX(), coordinate.getY(), Algorithms.NO_EDGE);
        }

        return movementPenaltyTable;
    }
}
