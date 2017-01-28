package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.terrain.Terrain;

public interface ActorFactory {

  TerrainActor create(Terrain terrain, EventListener eventListener);

  PlayerActor create(Player player, EventListener eventListener);

  EnemyActor create(Enemy enemy, EventListener eventListener);
}
