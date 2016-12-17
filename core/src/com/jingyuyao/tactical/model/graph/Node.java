package com.jingyuyao.tactical.model.graph;

import com.jingyuyao.tactical.model.MapObject;

abstract class Node<T extends MapObject> implements Comparable<Node<T>> {
    private final T mapObject;
    private Node<T> parent;
    private int distance;

    Node(T mapObject, Node<T> parent, int distance) {
        this.mapObject = mapObject;
        this.parent = parent;
        this.distance = distance;
    }

    public T getMapObject() {
        return mapObject;
    }

    Node<T> getParent() {
        return parent;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    /**
     * Changes the parent to {@code newParent}. Removes this node from the previous parent if it exists.
     * @param newParent The new parent of this node
     */
    public void changeParent(Node<T> newParent) {
        if (getParent() != null) {
            getParent().removeChild(this);
        }
        setParent(newParent);
    }

    private void setParent(Node<T> parent) {
        this.parent = parent;
    }

    @Override
    public int compareTo(Node<T> node) {
        return getDistance() - node.getDistance();
    }

    protected abstract void removeChild(Node<T> child);
}
