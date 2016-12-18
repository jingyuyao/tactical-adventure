package com.jingyuyao.tactical.model.graph;

import com.jingyuyao.tactical.model.HasCoordinate;

/**
 * A node in the graph that contains an {@code object}, a {@code parent} and a cumulative {@code cumulativeEdgeCost}
 * in relation to somewhere.
 * @param <T> The data type this node contains
 */
abstract class Node<T extends HasCoordinate> implements Comparable<Node<T>> {
    private final T object;
    private Node<T> parent;
    private int cumulativeEdgeCost;

    Node(T object, Node<T> parent, int cumulativeEdgeCost) {
        this.object = object;
        this.parent = parent;
        this.cumulativeEdgeCost = cumulativeEdgeCost;
    }

    @Override
    public int compareTo(Node<T> node) {
        return getCumulativeEdgeCost() - node.getCumulativeEdgeCost();
    }

    T getObject() {
        return object;
    }

    int getCumulativeEdgeCost() {
        return cumulativeEdgeCost;
    }

    void setCumulativeEdgeCost(int cumulativeEdgeCost) {
        this.cumulativeEdgeCost = cumulativeEdgeCost;
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
                ", cumulativeEdgeCost=" + cumulativeEdgeCost +
                '}';
    }
}
