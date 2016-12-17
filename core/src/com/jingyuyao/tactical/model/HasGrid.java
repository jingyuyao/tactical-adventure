package com.jingyuyao.tactical.model;

public interface HasGrid<T extends HasCoordinate> {
    T get(int x, int y);

    void set(T data, int x, int y);

    int getWidth();

    int getHeight();
}
