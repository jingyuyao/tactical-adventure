package com.jingyuyao.tactical.model;

import com.google.common.base.Preconditions;
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
        deselect();
        character.setPosition(targetTerrain.getX(), targetTerrain.getY());
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

    private void showReachableTerrains(Character character) {
        Terrain characterTerrain = getTerrain(character.getX(), character.getY());
        Path reachablePath = getReachablePath(characterTerrain, character.getMoveDistance());
        // TODO: Set correct potential target depending on character type i.e. enemy vs player
        for (Terrain terrain : reachablePath.getReachableTerrains()) {
            terrain.setPotentialTarget(Terrain.PotentialTarget.REACHABLE);
        }
        call(Calls.TERRAINS_UPDATE);
    }

    private void hideReachableTerrain(Character character) {
        Terrain characterTerrain = getTerrain(character.getX(), character.getY());
        Path reachablePath = getReachablePath(characterTerrain, character.getMoveDistance());
        for (Terrain terrain : reachablePath.getReachableTerrains()) {
            terrain.setPotentialTarget(Terrain.PotentialTarget.NONE);
        }
        call(Calls.TERRAINS_UPDATE);
    }

    /**
     * Dijkstra's path finding up to {@code moveDistance} weight
     */
    private Path getReachablePath(Terrain startingTerrain, int moveDistance) {
        java.util.Map<Terrain, Path> pathMap = new HashMap<Terrain, Path>();
        PriorityQueue<Path> pathQueue = new PriorityQueue<Path>();
        Path startingPath = new Path(startingTerrain, null, 0);
        Set<Terrain> visitedTerrains = new HashSet<Terrain>();

        pathQueue.add(startingPath);
        while (!pathQueue.isEmpty()) {
            Path minPath = pathQueue.poll();
            visitedTerrains.add(minPath.getTerrain());

            for (Terrain terrain : getAdjacentTerrain(minPath.getTerrain())) {
                if (visitedTerrains.contains(terrain)) {
                    continue;
                }

                // TODO: Take terrain type into weight consideration
                int distance = minPath.getDistance() + 1;
                if (distance > moveDistance) {
                    continue;
                }

                Path nextPath;
                if (pathMap.containsKey(terrain)) {
                    nextPath = pathMap.get(terrain);
                    pathQueue.remove(nextPath);
                    if (distance < nextPath.getDistance()) {
                        nextPath.updateParent(minPath, distance);
                        minPath.addNextPath(nextPath);
                    }
                } else {
                    nextPath = new Path(terrain, minPath, distance);
                    minPath.addNextPath(nextPath);
                    pathMap.put(terrain, nextPath);
                }
                pathQueue.add(nextPath);
            }
        }

        return startingPath;
    }

    private List<Terrain> getAdjacentTerrain(Terrain terrain) {
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
