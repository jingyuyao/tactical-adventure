package com.jingyuyao.tactical.model.graph;

import com.google.common.base.Objects;

import java.util.PriorityQueue;

/**
 * Store an object with an integer value. Identity of this object is based off {@link #object}.
 * {@link #value} is not part of the identity.
 */
public class ValueNode<N> implements Comparable<ValueNode<N>> {
    private final N object;
    private final int value;

    ValueNode(N object, int value) {
        this.object = object;
        this.value = value;
    }

    public N getObject() {
        return object;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(ValueNode<N> other) {
        return value - other.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValueNode<?> valueNode = (ValueNode<?>) o;
        return com.google.common.base.Objects.equal(object, valueNode.object);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(object);
    }
}
