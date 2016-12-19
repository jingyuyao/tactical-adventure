package com.jingyuyao.tactical.model;

import java.util.ArrayList;
import java.util.Collection;

public class Map extends Grid<Terrain> {
    private final Selector selector;
    private final Highlighter highlighter;
    private final Collection<Character> characters;

    public Map(int width, int height) {
        super(width, height);
        characters = new ArrayList<Character>();
        selector = new Selector(this);
        highlighter = new Highlighter();
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

    void clearAllMarkers() {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                get(x, y).clearMarkers();
            }
        }
    }

    Grid<Integer> createMovementPenaltyGrid(Character character) {
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
