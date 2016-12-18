package com.jingyuyao.tactical.model.graph;

/**
 * @param <T> Additional data type used to calculate distance cost (i.e. a character)
 */
public interface HasDistanceCost<T> {
    int CANT_CROSS = -1;

    /**
     * @param data Additional data used to calculate distance cost
     * @return The distance cost or -1 if not possible
     */
    int getDistanceCost(T data);
}
