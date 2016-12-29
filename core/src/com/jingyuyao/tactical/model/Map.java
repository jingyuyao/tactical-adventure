package com.jingyuyao.tactical.model;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.graph.ValueGraph;
import com.jingyuyao.tactical.model.object.Character;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.Terrain;

import java.util.HashSet;
import java.util.Set;

public class Map {
    private final int width;
    private final int height;
    private final Grid<Terrain> terrains;
    private final Set<Player> players;
    private final Set<Enemy> enemies;

    public Map(EventBus eventBus, int width, int height) {
        this.width = width;
        this.height = height;
        terrains = new Grid<Terrain>(width, height);
        players = new HashSet<Player>();
        enemies = new HashSet<Enemy>();
        eventBus.register(this);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Grid<Terrain> getTerrains() {
        return terrains;
    }

    public void add(Player player) {
        players.add(player);
    }

    public void add(Enemy enemy) {
        enemies.add(enemy);
    }

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

    @Subscribe
    public void characterDeath(Character.Died died) {
        Iterables.removeIf(getCharacters(), Predicates.equalTo(died.getCharacter()));
    }
}
