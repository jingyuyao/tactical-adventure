package com.jingyuyao.tactical.model.graph;

import com.jingyuyao.tactical.model.HasCoordinate;

abstract class Node<T extends HasCoordinate> implements Comparable<Node<T>> {
    private final T mapObject;
    private Node<T> parent;
    private int distance;

    Node(T mapObject, Node<T> parent, int distance) {
        this.mapObject = mapObject;
        this.parent = parent;
        this.distance = distance;
    }

    @Override
    public int compareTo(Node<T> node) {
        return getDistance() - node.getDistance();
    }

    T getMapObject() {
        return mapObject;
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
    void changeParent(Node<T> newParent) {
        if (getParent() != null) {
            getParent().removeChild(this);
        }
        setParent(newParent);
    }

    private Node<T> getParent() {
        return parent;
    }

    private void setParent(Node<T> parent) {
        this.parent = parent;
    }

    protected abstract void removeChild(Node<T> child);

    @Override
    public String toString() {
        return "Node{" +
                "mapObject=" + mapObject +
                ", distance=" + distance +
                '}';
    }
}
