package com.jingyuyao.tactical.view.actor;

import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.view.resource.LoopAnimation;

public interface ActorFactory {

  CellActor create(Cell cell, Coordinate initialCoordinate);

  TerrainActor create(Terrain terrain, Coordinate initialCoordinate);

  PlayerActor create(Player player, Coordinate initialCoordinate, LoopAnimation loopAnimation);

  EnemyActor create(Enemy enemy, Coordinate initialCoordinate, LoopAnimation loopAnimation);
}
