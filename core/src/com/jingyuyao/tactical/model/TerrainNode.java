package com.jingyuyao.tactical.model;

abstract class TerrainNode implements Comparable<TerrainNode> {
    private final Terrain terrain;
    private TerrainNode parent;
    private int distance;

    TerrainNode(Terrain terrain, TerrainNode parent, int distance) {
        this.terrain = terrain;
        this.parent = parent;
        this.distance = distance;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    TerrainNode getParent() {
        return parent;
    }

    int getDistance() {
        return distance;
    }

    void setDistance(int distance) {
        this.distance = distance;
    }

    /**
     * Changes the parent to {@code newParent}. Removes this node from the previous parent if it exists.
     * @param newParent The new parent of this node
     */
    void changeParent(TerrainNode newParent) {
        if (getParent() != null) {
            getParent().removeChild(this);
        }
        setParent(newParent);
    }

    private void setParent(TerrainNode parent) {
        this.parent = parent;
    }

    @Override
    public int compareTo(TerrainNode node) {
        return getDistance() - node.getDistance();
    }

    protected abstract void removeChild(TerrainNode child);
}
