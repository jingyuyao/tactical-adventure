package com.jingyuyao.tactical.model;

import com.google.common.base.Objects;
import com.google.common.graph.Graph;
import com.google.common.graph.ValueGraph;

import java.util.ArrayList;
import java.util.Collection;

public class Map {
    private final int width;
    private final int height;
    private final Highlighter highlighter;
    private final Collection<Character> characters;
    private final Grid<Terrain> terrains;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        terrains = new Grid<Terrain>(width, height);
        characters = new ArrayList<Character>();
        highlighter = new Highlighter();
    }

    public Terrain getTerrain(int x, int y) {
        return terrains.get(x, y);
    }

    public void setTerrain(int x, int y, Terrain terrain) {
        terrains.set(x, y ,terrain);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Highlighter getHighlighter() {
        return highlighter;
    }

    public void addCharacter(Character character) {
        characters.add(character);
    }

    public Collection<Character> getCharacters() {
        return characters;
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

    public Collection<Terrain> getAllTargetTerrains(Character character) {
        Graph<Terrain> moveTerrains = getMoveGraph(character);
        Collection<Terrain> targetTerrains = new ArrayList<Terrain>();

        for (Terrain terrain : moveTerrains.nodes()) {
            targetTerrains.addAll(getTargetTerrains(character, terrain));
        }

        return targetTerrains;
    }

    public Collection<Terrain> getTargetTerrains(Character character, Terrain source) {
        Collection<Terrain> targetTerrains = new ArrayList<Terrain>();
        for (Weapon weapon : character.getWeapons()) {
            for (int distance : weapon.getAttackDistances()) {
                targetTerrains.addAll(
                        Algorithms.findNDistanceAway(terrains, source.getX(), source.getY(), distance)
                );
            }
        }
        return targetTerrains;
    }

    /**
     * Return whether {@code source} has anything it can target from its current position.
     */
    public boolean hasAnyTarget(Character source) {
        Collection<Terrain> targetTerrains = getTargetTerrains(source, getTerrain(source.getX(), source.getY()));
        // TODO: grid for terrain and grid for characters?
        for (Terrain terrain : targetTerrains) {
            for (Character c : characters) {
                // TODO: Make a method like: character.canTarget(Character another)
                if (c.getX() == terrain.getX() && c.getY() == terrain.getY()
                        && !Objects.equal(c.getClass(), source.getClass())) {
                    return true;
                }
            }
        }
        return false;
    }

    public ValueGraph<Terrain, Integer> getMoveGraph(Character character) {
        return Algorithms.minPathSearch(
                terrains,
                createMovementPenaltyGrid(character),
                character.getX(),
                character.getY(),
                character.getMovementDistance());
    }

    private Grid<Integer> createMovementPenaltyGrid(Character character) {
        Grid<Integer> movementPenaltyGrid = new Grid<Integer>(getWidth(), getHeight());

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                Terrain terrain = getTerrain(x, y);
                movementPenaltyGrid.set(x, y, terrain.getMovementPenalty(character));
            }
        }

        for (Character c : getCharacters()) {
            movementPenaltyGrid.set(c.getX(), c.getY(), Algorithms.NO_EDGE);
        }

        return movementPenaltyGrid;
    }
}
