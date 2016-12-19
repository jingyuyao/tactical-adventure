package com.jingyuyao.tactical.model;

import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.graph.*;

import java.util.ArrayList;
import java.util.Collection;

public class Map extends Grid<Terrain> {
    private final Collection<Character> characters;
    private MapObject highlighted;
    private SelectionState selectionState = SelectionState.NONE;
    private Character selectedCharacter;

    public Map(int width, int height) {
        super(width, height);
        this.characters = new ArrayList<Character>();
    }

    public void addCharacter(Character character) {
        characters.add(character);
    }

    public Collection<Character> getCharacters() {
        return characters;
    }

    public MapObject getHighlighted() {
        return highlighted;
    }

    public void select(Character character) {
        Terrain.SelectionMode selectionMode = Terrain.SelectionMode.NONE;
        switch (character.getType()) {
            case PLAYER:
                selectionMode = Terrain.SelectionMode.MOVE;
                break;
            case ENEMY:
                selectionMode = Terrain.SelectionMode.DANGER;
                break;
        }

        switch (selectionState) {
            case CHARACTER:
                setAllPathsTo(selectedCharacter, Terrain.SelectionMode.NONE);
                if (character.equals(selectedCharacter)) {
                    selectionState = SelectionState.NONE;
                    return;
                }
            case NONE:
            case TERRAIN:
                setAllPathsTo(character, selectionMode);
                selectedCharacter = character;
                selectionState = SelectionState.CHARACTER;
                break;
        }
    }

    public void select(Terrain terrain) {
        switch (selectionState) {
            case CHARACTER:
                switch (terrain.getSelectionMode()) {
                    case MOVE:
                        setAllPathsTo(selectedCharacter, Terrain.SelectionMode.NONE);
                        moveCharacter(selectedCharacter, terrain);
                        break;
                    case NONE:
                    case DANGER:
                        setAllPathsTo(selectedCharacter, Terrain.SelectionMode.NONE);
                        break;
                    case ATTACK:
                        break;
                }
            case NONE:
            case TERRAIN:
                selectionState = SelectionState.TERRAIN;
                break;
        }
    }

    public void setHighlighted(MapObject newHighlight) {
        highlighted = newHighlight;
    }

    private void moveCharacter(Character character, Terrain terrain) {
        int x = terrain.getX();
        int y = terrain.getY();
        Graph<Terrain> allPaths = findAllPathsFor(character);
        Optional<Path<Terrain>> pathToCoordinate = allPaths.getPathTo(x, y);
        if (pathToCoordinate.isPresent()) {
            character.moveTo(x, y, pathToCoordinate.get());
        }
    }

    private void setAllPathsTo(Character character, Terrain.SelectionMode mode) {
        // TODO: Set correct selection mode depending on character type i.e. enemy vs player
        for (Terrain terrain : findAllPathsFor(character).getAllObjects()) {
            terrain.setSelectionMode(mode);
        }
    }

    private Graph<Terrain> findAllPathsFor(Character character) {
        return GraphAlgorithms.findAllPath(
                this,
                createEdgeCostGrid(character),
                character.getX(),
                character.getY(),
                character.getMovementDistance());
    }

    private Grid<Integer> createEdgeCostGrid(Character character) {
        Grid<Integer> edgeCostGrid = new Grid<Integer>(getWidth(), getHeight());

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                Terrain terrain = get(x, y);
                edgeCostGrid.set(x, y, terrain.getMovementPenality(character));
            }
        }

        for (Character c : getCharacters()) {
            edgeCostGrid.set(c.getX(), c.getY(), GraphAlgorithms.NO_EDGE);
        }

        return edgeCostGrid;
    }

    public enum SelectionState {
        NONE, CHARACTER, TERRAIN
    }
}
