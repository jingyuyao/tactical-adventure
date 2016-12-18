package com.jingyuyao.tactical.model;

import com.jingyuyao.tactical.model.graph.Graph;
import com.jingyuyao.tactical.model.graph.GraphFactory;
import com.jingyuyao.tactical.model.graph.Path;

import java.util.ArrayList;
import java.util.Collection;

public class Map implements HasGrid<Terrain> {
    /**
     * (0,0) starts at bottom left.
     */
    private final int width;
    private final int height;
    private final Terrain[][] terrains;
    private final Collection<Character> characters;
    private MapObject highlighted;
    private SelectionState selectionState = SelectionState.NONE;
    private Character selectedCharacter;

    Map(int width, int height) {
        this.width = width;
        this.height = height;
        this.terrains = new Terrain[height][width];
        this.characters = new ArrayList<Character>();
    }

    @Override
    public void set(Terrain terrain, int x, int y) {
        terrains[y][x] = terrain;
    }

    @Override
    public Terrain get(int x, int y) {
        return terrains[y][x];
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    void addCharacter(Character character) {
        characters.add(character);
    }

    public Collection<Character> getCharacters() {
        return characters;
    }

    public MapObject getHighlighted() {
        return highlighted;
    }

    public void select(Character character) {
        switch (selectionState) {
            case CHARACTER:
                setReachableTerrains(selectedCharacter, Terrain.SelectionMode.NONE);
                if (character.equals(selectedCharacter)) {
                    selectionState = SelectionState.NONE;
                    return;
                }
            case NONE:
            case TERRAIN:
                setReachableTerrains(character, Terrain.SelectionMode.MOVE);
                selectedCharacter = character;
                selectionState = SelectionState.CHARACTER;
                break;
        }
    }

    public void select(Terrain terrain) {
        switch (selectionState) {
            case CHARACTER:
                switch (terrain.getSelectionMode()) {
                    case NONE:
                        setReachableTerrains(selectedCharacter, Terrain.SelectionMode.NONE);
                        break;
                    case MOVE:
                        setReachableTerrains(selectedCharacter, Terrain.SelectionMode.NONE);
                        moveCharacter(selectedCharacter, terrain);
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
        Graph<Terrain> reachableGraph = createReachableGraph(character);
        Path<Terrain> pathToCoordinate = reachableGraph.getPathTo(x, y);
        if (pathToCoordinate != null) {
            character.moveTo(x, y, pathToCoordinate);
        }
    }

    private void setReachableTerrains(Character character, Terrain.SelectionMode target) {
        // TODO: Set correct potential target depending on character type i.e. enemy vs player
        for (Terrain terrain : createReachableGraph(character).getAllObjects()) {
            terrain.setSelectionMode(target);
        }
    }

    private Graph<Terrain> createReachableGraph(Character character) {
        return GraphFactory.createReachableGraph(
                this, character.getX(), character.getY(), character.getTotalMoveCost(), character);
    }

    public enum SelectionState {
        NONE, CHARACTER, TERRAIN
    }
}
