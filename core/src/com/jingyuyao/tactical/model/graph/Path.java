package com.jingyuyao.tactical.model.graph;

import com.jingyuyao.tactical.model.HasCoordinate;

import java.util.ArrayList;
import java.util.Collection;

public class Path<T extends HasCoordinate> extends Node<T> {
    private Path<T> child;

    Path(T mapObject, Node<T> parent, int cumulativeEdgeCost) {
        super(mapObject, parent, cumulativeEdgeCost);
    }

    @Override
    protected void removeChild(Node<T> child) {
        setChild(null);
    }

    public Collection<T> getRoute() {
        Collection<T> objects = new ArrayList<T>();
        objects.add(getObject());
        Path<T> temp = child;
        while (temp != null) {
            objects.add(temp.getObject());
            temp = temp.getChild();
        }
        return objects;
    }

    void setChild(Path<T> child) {
        this.child = child;
    }

    private Path<T> getChild() {
        return child;
    }

    @Override
    public String toString() {
        return "Path{} " + super.toString();
    }
}