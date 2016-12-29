package com.jingyuyao.tactical.model;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.graph.ValueGraph;
import com.jingyuyao.tactical.model.object.Character;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.Terrain;
import com.jingyuyao.tactical.model.util.Disposed;

import java.util.Set;

public class Map {
    private final Grid<Terrain> terrains;
    private final Set<Player> players;
    private final Set<Enemy> enemies;

    // TODO: these all should be assisted injected so we'll have a MapFactory that can be used by loader
    public Map(EventBus eventBus, Grid<Terrain> terrains, Iterable<Player> players, Iterable<Enemy> enemies) {
        eventBus.register(this);
        this.terrains = terrains;
        // Defensive copy
        this.players = Sets.newHashSet(players);
        this.enemies = Sets.newHashSet(enemies);
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
    public void characterDeath(Disposed disposed) {
        Iterables.removeIf(getCharacters(), disposed.getMatchesPredicate());
    }
}
