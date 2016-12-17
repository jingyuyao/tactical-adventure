package com.jingyuyao.tactical.model;

import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.graph.Graph;
import com.jingyuyao.tactical.model.graph.Path;
import com.jingyuyao.tactical.util.HasCalls;

import java.util.*;

public class Map extends HasCalls<Map.Calls> {
    /**
     * (0,0) starts at bottom left.
     */
    private final int worldWidth;
    private final int worldHeight;
    private final Terrain[][] terrains;
    private final Collection<Character> characters;
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

    public Collection<Character> getCharacters() {
        return characters;
    }

    public Terrain getTerrain(int x, int y) {
        return terrains[y][x];
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
        showReachableTerrains(newSelected);
        selected = newSelected;
    }

    public void deselect() {
        if (selected != null) {
            hideReachableTerrain(selected);
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
                        return iy < worldHeight;
                    }

                    @Override
                    public Terrain next() {
                        Terrain terrain = getTerrain(ix, iy);

                        if (++ix == worldWidth) {
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

    private void showReachableTerrains(Character character) {
        // TODO: Set correct potential target depending on character type i.e. enemy vs player
        for (Terrain terrain : createReachableGraph(character).getAllObjects()) {
            terrain.setPotentialTarget(Terrain.PotentialTarget.REACHABLE);
        }
        call(Calls.TERRAINS_UPDATE);
    }

    private void hideReachableTerrain(Character character) {
        for (Terrain terrain : createReachableGraph(character).getAllObjects()) {
            terrain.setPotentialTarget(Terrain.PotentialTarget.NONE);
        }
        call(Calls.TERRAINS_UPDATE);
    }

    /**
     * Dijkstra's path finding up to {@code moveDistance} weight
     */
    private Graph<Terrain> createReachableGraph(Character character) {
        Terrain startingTerrain = getTerrain(character.getX(), character.getY());
        java.util.Map<Terrain, Graph<Terrain>> pathMap = new HashMap<Terrain, Graph<Terrain>>();
        PriorityQueue<Graph<Terrain>> minNodeQueue = new PriorityQueue<Graph<Terrain>>();
        Graph<Terrain> startingNode = new Graph<Terrain>(startingTerrain, null, 0);
        Set<MapObject> visitedTerrains = new HashSet<MapObject>();

        minNodeQueue.add(startingNode);
        while (!minNodeQueue.isEmpty()) {
            Graph<Terrain> minGraph = minNodeQueue.poll();
            visitedTerrains.add(minGraph.getMapObject());

            for (Terrain terrain : getAdjacentTerrain(minGraph.getMapObject())) {
                if (visitedTerrains.contains(terrain)) {
                    continue;
                }

                // TODO: Take terrain type into weight consideration
                int distance = minGraph.getDistance() + 1;
                if (distance > character.getMoveDistance()) {
                    continue;
                }

                Graph<Terrain> nextGraph;
                if (pathMap.containsKey(terrain)) {
                    nextGraph = pathMap.get(terrain);
                    minNodeQueue.remove(nextGraph);
                    if (distance < nextGraph.getDistance()) {
                        nextGraph.changeParent(minGraph);
                        nextGraph.setDistance(distance);
                        minGraph.addNextPath(nextGraph);
                    }
                } else {
                    nextGraph = new Graph<Terrain>(terrain, minGraph, distance);
                    minGraph.addNextPath(nextGraph);
                    pathMap.put(terrain, nextGraph);
                }
                minNodeQueue.add(nextGraph);
            }
        }

        return startingNode;
    }

    private List<Terrain> getAdjacentTerrain(MapObject terrain) {
        int x = terrain.getX();
        int y = terrain.getY();
        List<Terrain> adjacentTerrain = new ArrayList<Terrain>(4);

        if (x > 0) {
            adjacentTerrain.add(getTerrain(x - 1, y));
        }
        if (x < worldWidth - 1) {
            adjacentTerrain.add(getTerrain(x + 1, y));
        }
        if (y > 0) {
            adjacentTerrain.add(getTerrain(x, y - 1));
        }
        if (y < worldHeight - 1) {
            adjacentTerrain.add(getTerrain(x, y + 1));
        }

        return adjacentTerrain;
    }

    public enum Calls {
        CHARACTERS_UPDATE,
        TERRAINS_UPDATE
    }
}
