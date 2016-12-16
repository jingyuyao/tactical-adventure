package com.jingyuyao.tactical.model;

import java.util.ArrayList;
import java.util.List;

public class Map {
    /**
     * (0,0) starts at bottom left.
     */
    private final Terrain[][] terrains;
    private final List<Character> characters;
    private final int worldWidth;
    private final int worldHeight;
    private MapObject highlighted;
    private Character selected;

    Map(int worldWidth, int worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.terrains = new Terrain[worldHeight][worldWidth];
        this.characters = new ArrayList<Character>();
    }

    void setTerrain(Terrain terrain, int x, int y) {
        terrains[y][x] = terrain;
    }

    void addCharacter(Character character) {
        characters.add(character);
    }

    public int getWorldWidth() {
        return worldWidth;
    }

    public int getWorldHeight() {
        return worldHeight;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public Terrain getTerrain(int x, int y) {
        return terrains[y][x];
    }

    public Character getSelected() {
        return selected;
    }

    public void setSelected(Character selected) {
        this.selected = selected;
    }

    public MapObject getHighlighted() {
        return highlighted;
    }

    public void setHighlighted(MapObject highlighted) {
        this.highlighted = highlighted;
    }
}
