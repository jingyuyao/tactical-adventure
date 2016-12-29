package com.jingyuyao.tactical.model;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Table;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.util.DisposableObject;

import java.util.Iterator;

/**
 * A grid with elements of type {@link T} backed by a {@link Table}.
 * Contents of the table is cleared when {@link #dispose()} is called.
 * Also contains convenience methods to work with our {@link Coordinate} system.
 * @param <T> Type of the element
 */
// TODO: make this a singleton injectable for terrain
public class Grid<T> extends DisposableObject implements Iterable<T> {
    /**
     * (0,0) starts at bottom left.
     */
    private final Table<Integer, Integer, T> table;

    public Grid(EventBus eventBus, Table<Integer, Integer, T> table) {
        super(eventBus);
        this.table = table;
    }

    @Override
    protected void disposed() {
        table.clear();
        super.disposed();
    }

    public int getWidth() {
        return Algorithms.tableWidth(table);
    }

    public int getHeight() {
        return Algorithms.tableHeight(table);
    }

    public T get(int x, int y) {
        return table.get(x, y);
    }

    public T get(Coordinate coordinate) {
        return get(coordinate.getX(), coordinate.getY());
    }

    public Iterable<T> getAll(Iterable<Coordinate> coordinates) {
        return Iterables.transform(coordinates, new Function<Coordinate, T>() {
            @Override
            public T apply(Coordinate input) {
                return get(input);
            }
        });
    }

    public void put(int x, int y, T data) {
        table.put(x, y, data);
    }

    @Override
    public Iterator<T> iterator() {
        return table.values().iterator();
    }
}
