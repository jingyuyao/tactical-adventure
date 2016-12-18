package com.jingyuyao.tactical.model;

import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.graph.Graph;
import com.jingyuyao.tactical.model.graph.GraphFactory;
import com.jingyuyao.tactical.model.graph.Path;
import com.jingyuyao.tactical.util.HasCalls;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Map extends HasCalls<Map.Calls> implements HasGrid<Terrain> {
    /**
     * (0,0) starts at bottom left.
     */
    private final int width;
    private final int height;
    private final Terrain[][] terrains;
    private final Collection<Character> characters;
    private MapObject highlighted;
    private Character selected;

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

    /**
     * Selects a character and display its reachable terrains.
     */
    public void select(Character newSelected) {
        Preconditions.checkNotNull(newSelected, "Don't call select with null");
        if (selected == newSelected) {
            deselect();
            return;
        }
        deselect();
        setReachableTerrains(newSelected, Terrain.PotentialTarget.REACHABLE);
        selected = newSelected;
    }

    public void deselect() {
        if (selected != null) {
            setReachableTerrains(selected, Terrain.PotentialTarget.NONE);
            selected = null;
        }
    }

    public void highlight(MapObject newHighlight) {
        if (highlighted != newHighlight) {
            if (highlighted != null) {
                highlighted.setHighlighted(false);
            }
            newHighlight.setHighlighted(true);
            highlighted = newHighlight;
        }
    }

    public void moveSelectedTo(Terrain targetTerrain) {
        Character character = selected;
        // Needs to be called before character moves so reachable terrains can be hidden correctly
        deselect();
        moveCharacter(character, targetTerrain);
        call(Calls.CHARACTERS_UPDATE);
    }

    /**
     * Convenience method to get all the terrains.
     */
    public Iterable<Terrain> getTerrains() {
        return new Iterable<Terrain>() {
            @Override
            public Iterator<Terrain> iterator() {
                return new Iterator<Terrain>() {
                    private int ix = 0;
                    private int iy = 0;

                    @Override
                    public boolean hasNext() {
                        return iy < height;
                    }

                    @Override
                    public Terrain next() {
                        Terrain terrain = get(ix, iy);

                        if (++ix == width) {
                            ix = 0;
                            iy++;
                        }

                        return terrain;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("nah");
                    }
                };
            }
        };
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

    private void setReachableTerrains(Character character, Terrain.PotentialTarget target) {
        // TODO: Set correct potential target depending on character type i.e. enemy vs player
        for (Terrain terrain : createReachableGraph(character).getAllObjects()) {
            terrain.setPotentialTarget(target);
        }
        call(Calls.TERRAINS_UPDATE);
    }

    private Graph<Terrain> createReachableGraph(Character character) {
        return GraphFactory.createReachableGraph(
                this, character.getX(), character.getY(), character.getTotalMoveCost(), character);
    }

    public enum Calls {
        CHARACTERS_UPDATE,
        TERRAINS_UPDATE
    }
}
