package com.jingyuyao.tactical.model;

import java.util.ArrayList;
import java.util.Collection;

public class TerrainPath extends TerrainNode {
    private TerrainPath child;

    TerrainPath(Terrain terrain, TerrainNode parent, int distance) {
        super(terrain, parent, distance);
    }

    public Collection<Terrain> getRoute() {
        Collection<Terrain> terrains = new ArrayList<Terrain>();
        terrains.add(getTerrain());
        TerrainPath temp = child;
        while (temp != null) {
            terrains.add(temp.getTerrain());
            temp = temp.getChild();
        }
        return terrains;
    }

    void setChild(TerrainPath child) {
        this.child = child;
    }

    private TerrainPath getChild() {
        return child;
    }

    @Override
    protected void removeChild(TerrainNode child) {
        setChild(null);
    }
}
