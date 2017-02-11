package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.terrain.Terrain;

public interface ActorFactory {

  TerrainActor create(Terrain terrain);

  PlayerActor create(Player player, Sprite sprite);

  EnemyActor create(Enemy enemy, Sprite sprite);
}
