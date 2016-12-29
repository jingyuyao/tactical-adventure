package com.jingyuyao.tactical.model;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Table;
import com.google.common.graph.ValueGraph;
import com.jingyuyao.tactical.model.object.Character;
import com.jingyuyao.tactical.model.object.*;

import javax.inject.Inject;

public class Map {
    private final TerrainGrid terrains;
    private final CharacterContainer<Player> players;
    private final CharacterContainer<Enemy> enemies;

    @Inject
    public Map(TerrainGrid terrains, CharacterContainer<Player> players, CharacterContainer<Enemy> enemies) {
        this.terrains = terrains;
        this.players = players;
        this.enemies = enemies;
    }

    public int getWidth() {
        return terrains.getWidth();
    }

    public int getHeight() {
        return terrains.getHeight();
    }

    public TerrainGrid getTerrains() {
        return terrains;
    }

    // TODO: the ultimate goal after guice is to get rid of these things and make them singleton
    public Iterable<Player> getPlayers() {
        return players;
    }

    public Iterable<Enemy> getEnemies() {
        return enemies;
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
        Table<Integer, Integer, Integer> movementPenaltyTable = HashBasedTable.create(getWidth(), getHeight());

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
