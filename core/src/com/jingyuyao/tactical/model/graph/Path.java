package com.jingyuyao.tactical.model.graph;

import com.jingyuyao.tactical.model.MapObject;

import java.util.ArrayList;
import java.util.Collection;

public class Path<T extends MapObject> extends Node<T> {
    private Path<T> child;

    Path(T mapObject, Node<T> parent, int distance) {
        super(mapObject, parent, distance);
    }

    public Collection<T> getRoute() {
        Collection<T> terrains = new ArrayList<T>();
        terrains.add(getMapObject());
        Path<T> temp = child;
        while (temp != null) {
            terrains.add(temp.getMapObject());
            temp = temp.getChild();
        }
        return terrains;
    }

    void setChild(Path<T> child) {
        this.child = child;
    }

    private Path<T> getChild() {
        return child;
    }

    @Override
    protected void removeChild(Node<T> child) {
        setChild(null);
    }
}
