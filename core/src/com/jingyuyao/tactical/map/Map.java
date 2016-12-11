package com.jingyuyao.tactical.map;

import com.google.common.base.Preconditions;

/**
 * TODO: Make a factory that builds a map from a TiledMap
 */
public class Map {
    private Block[][] blocks;
    private int height;
    private int width;

    public Map(int height, int width)    {
        Preconditions.checkArgument(height>0);
        Preconditions.checkArgument(width>0);
        blocks = new Block[height][width];
        this.height = height;
        this.width = width;
    }

    public void set(Block block, int x, int y) {
        Preconditions.checkNotNull(block);
        checkBounds(x, y);
        blocks[y][x] = block;
    }

    public Block get(int x, int y) {
        checkBounds(x, y);
        return blocks[y][x];
    }

    private void checkBounds(int x, int y) {
        Preconditions.checkArgument(x>0 && x<width);
        Preconditions.checkArgument(y>0 && y<height);
    }
}
