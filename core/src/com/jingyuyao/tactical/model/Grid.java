package com.jingyuyao.tactical.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A grid with elements of type {@link T}.
 * @param <T> Type of the element
 */
public class Grid<T> {
    /**
     * (0,0) starts at bottom left.
     */
    private final List<List<T>> rows;
    private final int width;
    private final int height;

    /**
     * Creates an empty grid with the given {@code width} and {@code height}.
     * Initial elements are set to null.
     */
    Grid(int width, int height) {
        this.width = width;
        this.height = height;
        rows = new ArrayList<List<T>>(height);
        for (int i = 0; i < height; i++) {
            List<T> row = new ArrayList<T>(width);
            for (int j = 0; j < width; j++) {
                row.add(null);
            }
            rows.add(row);
        }
    }

    public T get(int x, int y) {
        return rows.get(y).get(x);
    }

    public void set(int x, int y, T data) {
        rows.get(y).set(x, data);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Returns the in-bound neighbors of ({@code x}, {@code y}).
     * Order of returned neighbors is randomized.
     * @return Randomized list of neighbors
     */
    public List<T> getNeighbors(int x, int y) {
        List<T> neighbors = new ArrayList<T>(4);

        if (x > 0) {
            neighbors.add(get(x - 1, y));
        }
        if (x < getWidth() - 1) {
            neighbors.add(get(x + 1, y));
        }
        if (y > 0) {
            neighbors.add(get(x, y - 1));
        }
        if (y < getHeight() - 1) {
            neighbors.add(get(x, y + 1));
        }

        Collections.shuffle(neighbors);

        return neighbors;
    }
}
