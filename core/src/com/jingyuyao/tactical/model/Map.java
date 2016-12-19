package com.jingyuyao.tactical.model;

import com.jingyuyao.tactical.model.graph.*;

import java.util.ArrayList;
import java.util.Collection;

public class Map extends Grid<Terrain> {
    private final MapLogic logic;
    private final Collection<Character> characters;
    private MapObject highlighted;

    public Map(int width, int height) {
        super(width, height);
        characters = new ArrayList<Character>();
        logic = new MapLogic(this);
    }

    public MapLogic getLogic() {
        return logic;
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

    public void setHighlighted(MapObject newHighlight) {
        highlighted = newHighlight;
    }

    void clearAllMarkers() {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                get(x, y).clearMarkers();
            }
        }
    }

    Grid<Integer> createEdgeCostGrid(Character character) {
        Grid<Integer> edgeCostGrid = new Grid<Integer>(getWidth(), getHeight());

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                Terrain terrain = get(x, y);
                edgeCostGrid.set(x, y, terrain.getMovementPenalty(character));
            }
        }

        for (Character c : getCharacters()) {
            edgeCostGrid.set(c.getX(), c.getY(), GraphAlgorithms.NO_EDGE);
        }

        return edgeCostGrid;
    }
}
