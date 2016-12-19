package com.jingyuyao.tactical.model.graph;

import com.jingyuyao.tactical.model.HasCoordinate;

/**
 * A node in a graph that contains an {@code object}, a {@code parent} and a {@code pathCost}
 * in relation to somewhere.
 * @param <T> The data type this node contains
 */
abstract class Node<T extends HasCoordinate> implements Comparable<Node<T>> {
    private final T object;
    private Node<T> parent;
    /**
     * Path cost is the cumulative edge cost from some where to this node.
     */
    private int pathCost;

    Node(T object, Node<T> parent, int pathCost) {
        this.object = object;
        this.parent = parent;
        this.pathCost = pathCost;
    }

    @Override
    public int compareTo(Node<T> node) {
        return getPathCost() - node.getPathCost();
    }

    T getObject() {
        return object;
    }

    int getPathCost() {
        return pathCost;
    }

    void setPathCost(int pathCost) {
        this.pathCost = pathCost;
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
                "object=" + object +
                ", pathCost=" + pathCost +
                '}';
    }
}
