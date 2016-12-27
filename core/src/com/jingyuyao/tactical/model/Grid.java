package com.jingyuyao.tactical.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A grid with elements of type {@link T}.
 * @param <T> Type of the element
 */
public class Grid<T> implements Iterable<T> {
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public T get(int x, int y) {
        return rows.get(y).get(x);
    }

    public T get(Coordinate coordinate) {
        return get(coordinate.getX(), coordinate.getY());
    }

    public void set(int x, int y, T data) {
        rows.get(y).set(x, data);
    }

    public void set(Coordinate coordinate, T data) {
        set(coordinate.getX(), coordinate.getY(), data);
    }

    /**
     * Returns the in-bound neighbors of {@code from}.
     * @return Randomized list of neighbors
     */
    ImmutableList<Coordinate> getNeighbors(Coordinate from) {
        int x = from.getX();
        int y = from.getY();

        List<Coordinate> neighbors = new ArrayList<Coordinate>(4);

        if (x > 0) {
            neighbors.add(new Coordinate(x - 1, y));
        }
        if (x < getWidth() - 1) {
            neighbors.add(new Coordinate(x + 1, y));
        }
        if (y > 0) {
            neighbors.add(new Coordinate(x, y - 1));
        }
        if (y < getHeight() - 1) {
            neighbors.add(new Coordinate(x, y + 1));
        }

        Collections.shuffle(neighbors);
        return ImmutableList.copyOf(neighbors);
    }

    /**
     * Get the coordinates that "look" like {@code distance} away. This method is the same as
     * {@link #getNeighbors(Coordinate)} if {@code distance==1}. Otherwise it will return
     * a max list of eight neighbors that looks like they are {@code distance} away.
     *
     * @param from starting coordinate
     */
    ImmutableList<Coordinate> getNDistanceAway(Coordinate from, int distance) {
        if (distance == 1) {
            return getNeighbors(from);
        }

        int x = from.getX();
        int y = from.getY();
        ImmutableList.Builder<Coordinate> builder = new ImmutableList.Builder<Coordinate>();

        if (x - distance >= 0) {
            builder.add(new Coordinate(x - distance, y)); // left
        }
        if (x + distance < getWidth()) {
            builder.add(new Coordinate(x + distance, y)); // right
        }
        if (y - distance >= 0) {
            builder.add(new Coordinate(x, y - distance)); // down
        }
        if (y + distance < getHeight()) {
            builder.add(new Coordinate(x, y + distance)); // top
        }
        if (x - distance + 1 >= 0 && y - distance + 1 >= 0) {
            builder.add(new Coordinate(x - distance + 1, y - distance + 1)); // left down
        }
        if (x + distance - 1 < getWidth() && y - distance + 1 >= 0) {
            builder.add(new Coordinate(x + distance - 1, y - distance + 1)); // right down
        }
        if (x - distance + 1 >= 0 && y + distance - 1 < getHeight()) {
            builder.add(new Coordinate(x - distance + 1, y + distance - 1)); // left top
        }
        if (x + distance - 1 < getWidth() && y + distance - 1 < getHeight()) {
            builder.add(new Coordinate(x + distance - 1, y + distance - 1)); // right top
        }

        return builder.build();
    }

    @Override
    public Iterator<T> iterator() {
        return Iterables.concat(rows).iterator();
    }
}
