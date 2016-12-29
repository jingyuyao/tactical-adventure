package com.jingyuyao.tactical.model;

import com.google.common.collect.Iterables;
import com.google.common.graph.ValueGraph;
import com.jingyuyao.tactical.model.object.Character;
import com.jingyuyao.tactical.model.object.*;

public class Map {
    private final Grid<Terrain> terrains;
    private final CharacterContainer<Player> players;
    private final CharacterContainer<Enemy> enemies;

    // TODO: these all should be assisted injected so we'll have a MapFactory that can be used by loader
    public Map(Grid<Terrain> terrains, CharacterContainer<Player> players, CharacterContainer<Enemy> enemies) {
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

    public Grid<Terrain> getTerrains() {
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
                createMovementPenaltyGrid(character),
                character.getCoordinate(),
                character.getMoveDistance());
    }

    private Grid<Integer> createMovementPenaltyGrid(Character character) {
        Grid<Integer> movementPenaltyGrid = new Grid<Integer>(getWidth(), getHeight());

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                Terrain terrain = getTerrains().get(x, y);
                movementPenaltyGrid.set(terrain.getCoordinate(), terrain.getMovementPenalty(character));
            }
        }

        for (Character blocked : getCharacters()) {
            movementPenaltyGrid.set(blocked.getCoordinate(), Algorithms.NO_EDGE);
        }

        return movementPenaltyGrid;
    }
}
