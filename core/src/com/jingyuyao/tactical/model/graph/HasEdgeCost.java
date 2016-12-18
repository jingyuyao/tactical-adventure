package com.jingyuyao.tactical.model.graph;

/**
 * @param <T> Additional data type used to calculate edge cost (i.e. a character)
 */
public interface HasEdgeCost<T> {
    int NO_EDGE = -1;

    /**
     * @param data Additional data used to calculate edge cost with
     * @return The edge cost or {@link #NO_EDGE} if not possible
     */
    int getEdgeCost(T data);
}
