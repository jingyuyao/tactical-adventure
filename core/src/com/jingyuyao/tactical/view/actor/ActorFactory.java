package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Terrain;

public interface ActorFactory {

  PlayerActor createPlayerActor(Player player, EventListener eventListener);

  EnemyActor createEnemyActor(Enemy enemy, EventListener eventListener);

  TerrainActor createTerrainActor(Terrain terrain, EventListener eventListener);
}
