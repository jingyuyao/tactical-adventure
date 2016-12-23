package com.jingyuyao.tactical.model;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.graph.Graph;
import com.google.common.graph.ValueGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Map extends Observable {
    private final int width;
    private final int height;
    private final List<Character> characters;
    private final Grid<Terrain> terrains;
    private MapObject highlight;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        terrains = new Grid<Terrain>(width, height);
        characters = new ArrayList<Character>();
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

    // TODO: make this immutable
    public List<Character> getCharacters() {
        return characters;
    }

    public ImmutableCollection<Player> getPlayers() {
        return getSubtypeCharacters(Player.class);
    }

    public ImmutableCollection<Enemy> getEnemies() {
        return getSubtypeCharacters(Enemy.class);
    }

    public MapObject getHighlight() {
        return highlight;
    }

    public void setHighlight(MapObject highlight) {
        this.highlight = highlight;
        setChanged();
        notifyObservers();
    }

    public void kill(Character character) {
        getCharacters().remove(character);
        character.die();
    }

    /**
     * Move the {@code character} to {@code target} if possible.
     *
     * @return whether the {@code character} moved or not
     */
    public boolean moveIfAble(Character character, Coordinate target) {
        Graph<Coordinate> pathGraph = getMoveGraph(character);
        ImmutableList<Coordinate> pathToCoordinate = Algorithms.findPathTo(pathGraph, target);
        if (!pathToCoordinate.isEmpty()) {
            character.moveTo(target.getX(), target.getY(), pathToCoordinate);
            return true;
        }
        return false;
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

    public ValueGraph<Coordinate, Integer> getMoveGraph(Character character) {
        return Algorithms.minPathSearch(
                createMovementPenaltyGrid(character),
                character.getCoordinate(),
                character.getMovementDistance());
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
            // TODO: modify algorithm so it doesn't "backtrack"
            builder.addAll(Algorithms.findNDistanceAway(getTerrains(), from, distance));
        }
        return builder.build();
    }

    /**
     * Return the coordinate with the greatest number of weapon choices for target.
     */
    public Optional<Coordinate> getMoveForTarget(Character character, Coordinate target) {
        Graph<Coordinate> moveGraph = getMoveGraph(character);
        Coordinate currentBestTerrain = null;
        List<Weapon> currentMaxWeapons = new ArrayList<Weapon>();
        for (Coordinate source : moveGraph.nodes()) {
            List<Weapon> weaponsForThisTerrain = getWeaponsForTarget(character, source, target);
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

    @SuppressWarnings("unchecked")
    private <T extends Character> ImmutableCollection<T> getSubtypeCharacters(Class<T> cls) {
        ImmutableList.Builder<T> builder = new ImmutableList.Builder<T>();
        for (Character character : getCharacters()) {
            if (Objects.equal(character.getClass(), cls)) {
                builder.add((T) character);
            }
        }
        return builder.build();
    }
}
