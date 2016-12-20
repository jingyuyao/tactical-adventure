package com.jingyuyao.tactical.model;

import com.google.common.graph.Graph;
import com.google.common.graph.ValueGraph;

import java.util.ArrayList;
import java.util.Collection;

public class Map extends Grid<Terrain> {
    private final Selector selector;
    private final Highlighter highlighter;
    private final Selections selections;
    private final Collection<Character> characters;

    public Map(int width, int height) {
        super(width, height);
        characters = new ArrayList<Character>();
        highlighter = new Highlighter();
        selections = new Selections(this);
        selector = new Selector(this, selections);
    }

    public Selector getSelector() {
        return selector;
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

    void moveIfAble(Character character, Terrain terrain) {
        Graph<Terrain> pathGraph = getMoveGraph(character);
        Collection<Terrain> pathToCoordinate = Algorithms.findPathTo(pathGraph, terrain);
        if (!pathToCoordinate.isEmpty()) {
            character.moveTo(terrain.getX(), terrain.getY(), pathToCoordinate);
        }
    }

    void kill(Character character) {
        characters.remove(character);
        character.die();
        selections.removeEnemy(character);
    }

    Collection<Terrain> getAllTargetTerrains(Character character) {
        Graph<Terrain> moveTerrains = getMoveGraph(character);
        Collection<Terrain> targetTerrains = new ArrayList<Terrain>();

        for (Terrain terrain : moveTerrains.nodes()) {
            targetTerrains.addAll(getTargetTerrains(character, terrain));
        }

        return targetTerrains;
    }

    Collection<Terrain> getTargetTerrains(Character character, Terrain source) {
        Collection<Terrain> targetTerrains = new ArrayList<Terrain>();
        for (Weapon weapon : character.getWeapons()) {
            for (int distance : weapon.getAttackDistances()) {
                targetTerrains.addAll(
                        Algorithms.findNDistanceAway(this, source.getX(), source.getY(), distance)
                );
            }
        }
        return targetTerrains;
    }

    /**
     * Return whether {@code character} has anything it can target from its current position.
     */
    boolean hasAnyTarget(Character character) {
        Collection<Terrain> targetTerrains = getTargetTerrains(character, get(character.getX(), character.getY()));
        // TODO: grid for terrain and grid for characters?
        for (Terrain terrain : targetTerrains) {
            for (Character c : characters) {
                if (c.getX() == terrain.getX() && c.getY() == terrain.getY()
                        && !c.getType().equals(character.getType())) {
                    return true;
                }
            }
        }
        return false;
    }

    ValueGraph<Terrain, Integer> getMoveGraph(Character character) {
        return Algorithms.minPathSearch(
                this,
                createMovementPenaltyGrid(character),
                character.getX(),
                character.getY(),
                character.getMovementDistance());
    }

    private Grid<Integer> createMovementPenaltyGrid(Character character) {
        Grid<Integer> movementPenaltyGrid = new Grid<Integer>(getWidth(), getHeight());

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                Terrain terrain = get(x, y);
                movementPenaltyGrid.set(x, y, terrain.getMovementPenalty(character));
            }
        }

        for (Character c : getCharacters()) {
            movementPenaltyGrid.set(c.getX(), c.getY(), Algorithms.NO_EDGE);
        }

        return movementPenaltyGrid;
    }
}
