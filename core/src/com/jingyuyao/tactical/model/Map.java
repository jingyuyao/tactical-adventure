package com.jingyuyao.tactical.model;

import java.util.*;

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

    /**
     * Dijkstra's path finding up to {@code moveCount} weight
     */
    public Path getReachablePath(Terrain startingTerrain, int moveCount) {
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
                if (distance > moveCount) {
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
}
