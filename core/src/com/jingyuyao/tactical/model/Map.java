package com.jingyuyao.tactical.model;

import com.google.common.base.Optional;
import com.google.common.graph.Graph;
import com.google.common.graph.ValueGraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;

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

    public void moveIfAble(Character character, Terrain terrain) {
        Graph<Terrain> pathGraph = getMoveGraph(character);
        Collection<Terrain> pathToCoordinate = Algorithms.findPathTo(pathGraph, terrain);
        if (!pathToCoordinate.isEmpty()) {
            character.moveTo(terrain.getX(), terrain.getY(), pathToCoordinate);
        }
    }

    public void kill(Character character) {
        characters.remove(character);
        character.die();
    }

    public ValueGraph<Terrain, Integer> getMoveGraph(Character character) {
        return Algorithms.minPathSearch(
                terrains,
                createMovementPenaltyGrid(character),
                character,
                character.getMovementDistance());
    }

    public Collection<Coordinate> getAllTargets(Character character) {
        Graph<Terrain> moveTerrains = getMoveGraph(character);
        Collection<Coordinate> targets = new ArrayList<Coordinate>();
        for (Terrain terrain : moveTerrains.nodes()) {
            targets.addAll(getTargets(character, terrain));
        }
        return targets;
    }

    public Collection<Coordinate> getTargets(Character character, Coordinate source) {
        Collection<Coordinate> targets = new ArrayList<Coordinate>();
        for (Weapon weapon : character.getWeapons()) {
            targets.addAll(getTargetsForWeapon(weapon, source));
        }
        return targets;
    }

    public Collection<Coordinate> getTargetsForWeapon(Weapon weapon, Coordinate source) {
        Collection<Coordinate> targets = new ArrayList<Coordinate>();
        for (int distance : weapon.getAttackDistances()) {
            targets.addAll(Algorithms.findNDistanceAway(terrains, source, distance));
        }
        return targets;
    }

    /**
     * Get the best terrain to move to for hitting {@code target}. Best terrain is defined as the
     * terrain that can be hit with the most weapons.
     */
    public Optional<Terrain> getMoveTerrainForTarget(Character character, Coordinate target) {
        Graph<Terrain> moveGraph = getMoveGraph(character);
        Terrain currentBestTerrain = null;
        Collection<Weapon> currentMaxWeapons = new ArrayList<Weapon>();
        for (Terrain source : moveGraph.nodes()) {
            Collection<Weapon> weaponsForThisTerrain = getWeaponsForTarget(character, source, target);
            if (weaponsForThisTerrain.size() > currentMaxWeapons.size()) {
                currentMaxWeapons = weaponsForThisTerrain;
                currentBestTerrain = source;
            }
        }
        return Optional.fromNullable(currentBestTerrain);
    }

    public Collection<Weapon> getWeaponsForTarget(Character character, Coordinate source, Coordinate target) {
        Collection<Weapon> weaponsForThisTarget = new ArrayList<Weapon>();
        for (Weapon weapon : character.getWeapons()) {
            if (target.containedIn(getTargetsForWeapon(weapon, source))) {
                weaponsForThisTarget.add(weapon);
            }
        }
        return weaponsForThisTarget;
    }

    /**
     * Return whether {@code source} has anything it can target from its current position.
     */
    public boolean hasAnyTarget(Character source) {
        Collection<Coordinate> targets = getTargets(source, terrains.get(source));
        for (Coordinate target : targets) {
            for (Character c : characters) {
                if (target.sameCoordinateAs(c) && source.canTarget(c)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Grid<Integer> createMovementPenaltyGrid(Character character) {
        Grid<Integer> movementPenaltyGrid = new Grid<Integer>(getWidth(), getHeight());

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                Terrain terrain = terrains.get(x, y);
                movementPenaltyGrid.set(x, y, terrain.getMovementPenalty(character));
            }
        }

        for (Character c : characters) {
            movementPenaltyGrid.set(c, Algorithms.NO_EDGE);
        }

        return movementPenaltyGrid;
    }
}
