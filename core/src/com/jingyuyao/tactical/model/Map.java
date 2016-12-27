package com.jingyuyao.tactical.model;

import com.google.common.base.Optional;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.graph.Graph;
import com.google.common.graph.ValueGraph;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.object.Character;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.Terrain;

import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public class Map implements Observer {
    private final int width;
    private final int height;
    private final Grid<Terrain> terrains;
    private final Set<Player> players;
    private final Set<Enemy> enemies;
    private final MarkerManager markerManager;

    public Map(int width, int height, Waiter waiter) {
        this.width = width;
        this.height = height;
        terrains = new Grid<Terrain>(width, height);
        players = new HashSet<Player>();
        enemies = new HashSet<Enemy>();
        markerManager = new MarkerManager(this, waiter);
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
        player.addObserver(markerManager);
        player.addObserver(this);
    }

    public void add(Enemy enemy) {
        enemies.add(enemy);
        enemy.addObserver(markerManager);
        enemy.addObserver(this);
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

    /**
     * Return whether {@code from} has anything it can target from its current position.
     */
    public boolean hasAnyImmediateTarget(Character from) {
        for (Character to : getCharacters()) {
            if (canImmediateTarget(from, to)) {
                return true;
            }
        }
        return false;
    }

    public boolean canTargetAfterMove(Character from, Character to) {
        return from.canTarget(to) && getAllTargets(from).contains(to.getCoordinate());
    }

    public boolean canImmediateTarget(Character from, Character to) {
        return from.canTarget(to) && getTargetsFrom(from, from.getCoordinate()).contains(to.getCoordinate());
    }

    public ImmutableList<Coordinate> getPathToTarget(Character character, Coordinate target) {
        return Algorithms.findPathTo(getMoveGraph(character), target);
    }

    public ValueGraph<Coordinate, Integer> getMoveGraph(Character character) {
        return Algorithms.minPathSearch(
                createMovementPenaltyGrid(character),
                character.getCoordinate(),
                character.getMoveDistance());
    }

    public ImmutableSet<Coordinate> getAllTargets(Character character) {
        Graph<Coordinate> moveTerrains = getMoveGraph(character);
        ImmutableSet.Builder<Coordinate> builder = new ImmutableSet.Builder<Coordinate>();
        for (Coordinate terrain : moveTerrains.nodes()) {
            builder.addAll(getTargetsFrom(character, terrain));
        }
        return builder.build();
    }

    public ImmutableSet<Coordinate> getTargetsFrom(Character character, Coordinate from) {
        ImmutableSet.Builder<Coordinate> builder = new ImmutableSet.Builder<Coordinate>();
        for (Weapon weapon : character.getWeapons()) {
            builder.addAll(getTargetsForWeapon(weapon, from));
        }
        return builder.build();
    }

    public ImmutableSet<Coordinate> getTargetsForWeapon(Weapon weapon, Coordinate from) {
        ImmutableSet.Builder<Coordinate> builder = new ImmutableSet.Builder<Coordinate>();
        for (int distance : weapon.getAttackDistances()) {
            builder.addAll(getTerrains().getNDistanceAway(from, distance));
        }
        return builder.build();
    }

    /**
     * Return the coordinate with the greatest number of weapon choices for target.
     */
    public Optional<Coordinate> getMoveForTarget(Character character, Coordinate target) {
        Graph<Coordinate> moveGraph = getMoveGraph(character);
        Coordinate currentBestTerrain = null;
        ImmutableList<Weapon> currentMaxWeapons = ImmutableList.of();
        for (Coordinate source : moveGraph.nodes()) {
            ImmutableList<Weapon> weaponsForThisTerrain = getWeaponsForTarget(character, source, target);
            if (weaponsForThisTerrain.size() > currentMaxWeapons.size()) {
                currentMaxWeapons = weaponsForThisTerrain;
                currentBestTerrain = source;
            }
        }
        return Optional.fromNullable(currentBestTerrain);
    }

    public ImmutableList<Weapon> getWeaponsForTarget(Character character, Coordinate from, Coordinate target) {
        ImmutableList.Builder<Weapon> builder = new ImmutableList.Builder<Weapon>();
        for (Weapon weapon : character.getWeapons()) {
            if (getTargetsForWeapon(weapon, from).contains(target)) {
                builder.add(weapon);
            }
        }
        return builder.build();
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

    @Override
    public void update(Observable object, Object param) {
        if (Character.Died.class.isInstance(param)) {
            Iterables.removeIf(getCharacters(), Predicates.equalTo(object));
        }
    }
}
