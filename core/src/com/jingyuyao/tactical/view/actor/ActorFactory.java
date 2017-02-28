package com.jingyuyao.tactical.view.actor;

import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.view.resource.LoopAnimation;

public interface ActorFactory {

  TerrainActor create(Terrain terrain);

  PlayerActor create(Player player, LoopAnimation loopAnimation);

  EnemyActor create(Enemy enemy, LoopAnimation loopAnimation);
}
