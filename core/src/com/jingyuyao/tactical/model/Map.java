package com.jingyuyao.tactical.model;

import com.google.common.base.Optional;
import com.google.common.graph.Graph;
import com.google.common.graph.ValueGraph;

import java.util.*;

public class Map extends Observable {
    private final int width;
    private final int height;
    private final Collection<Character> characters;
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

    public Grid<Terrain> terrains() {
        return terrains;
    }

    public Collection<Character> characters() {
        return characters;
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
        characters.remove(character);
        character.die();
    }

    public void moveIfAble(Character character, Coordinate terrain) {
        Graph<Coordinate> pathGraph = getMoveGraph(character);
        Collection<Coordinate> pathToCoordinate = Algorithms.findPathTo(pathGraph, terrain);
        if (!pathToCoordinate.isEmpty()) {
            character.moveTo(terrain.getX(), terrain.getY(), pathToCoordinate);
        }
    }

    /**
     * Return whether {@code from} has anything it can target from its current position.
     */
    public boolean hasAnyImmediateTarget(Character from) {
        for (Character to : characters()) {
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

    public Collection<Coordinate> getAllTargets(Character character) {
        Graph<Coordinate> moveTerrains = getMoveGraph(character);
        Collection<Coordinate> targets = new HashSet<Coordinate>();
        for (Coordinate terrain : moveTerrains.nodes()) {
            targets.addAll(getTargetsFrom(character, terrain));
        }
        return targets;
    }

    public Collection<Coordinate> getTargetsFrom(Character character, Coordinate from) {
        Collection<Coordinate> targets = new HashSet<Coordinate>();
        for (Weapon weapon : character.getWeapons()) {
            targets.addAll(getTargetsForWeapon(weapon, from));
        }
        return targets;
    }

    public Collection<Coordinate> getTargetsForWeapon(Weapon weapon, Coordinate from) {
        Collection<Coordinate> targets = new HashSet<Coordinate>();
        for (int distance : weapon.getAttackDistances()) {
            // TODO: modify algorithm so it doesn't "backtrack"
            targets.addAll(Algorithms.findNDistanceAway(terrains, from, distance));
        }
        return targets;
    }

    /**
     * Return the coordinate with the greatest number of weapon choices for target.
     */
    public Optional<Coordinate> getMoveForTarget(Character character, Coordinate target) {
        Graph<Coordinate> moveGraph = getMoveGraph(character);
        Coordinate currentBestTerrain = null;
        Collection<Weapon> currentMaxWeapons = new ArrayList<Weapon>();
        for (Coordinate source : moveGraph.nodes()) {
            Collection<Weapon> weaponsForThisTerrain = getWeaponsForTarget(character, source, target);
            if (weaponsForThisTerrain.size() > currentMaxWeapons.size()) {
                currentMaxWeapons = weaponsForThisTerrain;
                currentBestTerrain = source;
            }
        }
        return Optional.fromNullable(currentBestTerrain);
    }

    public Collection<Weapon> getWeaponsForTarget(Character character, Coordinate from, Coordinate target) {
        Collection<Weapon> weaponsForThisTarget = new ArrayList<Weapon>();
        for (Weapon weapon : character.getWeapons()) {
            if (getTargetsForWeapon(weapon, from).contains(target)) {
                weaponsForThisTarget.add(weapon);
            }
        }
        return weaponsForThisTarget;
    }

    private Grid<Integer> createMovementPenaltyGrid(Character character) {
        Grid<Integer> movementPenaltyGrid = new Grid<Integer>(getWidth(), getHeight());

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                Terrain terrain = terrains().get(x, y);
                movementPenaltyGrid.set(terrain.getCoordinate(), terrain.getMovementPenalty(character));
            }
        }

        for (Character blocked : characters()) {
            movementPenaltyGrid.set(blocked.getCoordinate(), Algorithms.NO_EDGE);
        }

        return movementPenaltyGrid;
    }
}
