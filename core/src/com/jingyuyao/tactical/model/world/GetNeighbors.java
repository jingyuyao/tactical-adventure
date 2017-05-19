package com.jingyuyao.tactical.model.world;

interface GetNeighbors {

  Iterable<Cell> getNeighbors(Cell cell);
}
