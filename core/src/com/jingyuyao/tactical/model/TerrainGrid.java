package com.jingyuyao.tactical.model;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Table;
import com.google.common.eventbus.EventBus;
import com.google.inject.BindingAnnotation;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.util.DisposableObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Iterator;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * A terrain grid backed by a {@link Table}.
 * Contents of the table is cleared when {@link #dispose()} is called.
 * Also contains convenience methods to work with our {@link Coordinate} system.
 */
@Singleton
public class TerrainGrid extends DisposableObject implements Iterable<Terrain> {
    /**
     * (0,0) starts at bottom left.
     */
    private final Table<Integer, Integer, Terrain> table;

    @Inject
    public TerrainGrid(EventBus eventBus, @BackingTable Table<Integer, Integer, Terrain> table) {
        super(eventBus);
        this.table = table;
    }

    @Override
    public void dispose() {
        table.clear();
        super.dispose();
    }

    public int getWidth() {
        return Algorithms.tableWidth(table);
    }

    public int getHeight() {
        return Algorithms.tableHeight(table);
    }

    public Terrain get(int x, int y) {
        return table.get(x, y);
    }

    public Terrain get(Coordinate coordinate) {
        return get(coordinate.getX(), coordinate.getY());
    }

    public Iterable<Terrain> getAll(Iterable<Coordinate> coordinates) {
        return Iterables.transform(coordinates, new Function<Coordinate, Terrain>() {
            @Override
            public Terrain apply(Coordinate input) {
                return get(input);
            }
        });
    }

    public void put(int x, int y, Terrain data) {
        table.put(x, y, data);
    }

    @Override
    public Iterator<Terrain> iterator() {
        return table.values().iterator();
    }

    @BindingAnnotation @Target({FIELD, PARAMETER, METHOD}) @Retention(RUNTIME)
    public @interface BackingTable {}
}
