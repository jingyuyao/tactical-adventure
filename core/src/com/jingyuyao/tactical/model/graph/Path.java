package com.jingyuyao.tactical.model.graph;

import com.jingyuyao.tactical.model.HasCoordinate;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A sequence of nodes.
 */
public class Path<T extends HasCoordinate> extends Node<T> {
    private Path<T> child;

    Path(T mapObject, Node<T> parent, int pathCost) {
        super(mapObject, parent, pathCost);
    }

    @Override
    protected void removeChild(Node<T> child) {
        setChild(null);
    }

    /**
     * Get all the objects from this path in sequence.
     */
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
