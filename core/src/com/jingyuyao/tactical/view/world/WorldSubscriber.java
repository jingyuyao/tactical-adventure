package com.jingyuyao.tactical.view.world;

import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.event.AddEnemy;
import com.jingyuyao.tactical.model.event.AddPlayer;
import com.jingyuyao.tactical.model.event.AddTerrain;
import com.jingyuyao.tactical.model.event.RemoveObject;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldSubscriber {

  private final WorldView worldView;

  @Inject
  WorldSubscriber(WorldView worldView) {
    this.worldView = worldView;
  }

  @Subscribe
  void addTerrain(AddTerrain addTerrain) {
    worldView.add(addTerrain.getObject());
  }

  @Subscribe
  void addPlayer(AddPlayer addPlayer) {
    worldView.add(addPlayer.getObject());
  }

  @Subscribe
  void addEnemy(AddEnemy addEnemy) {
    worldView.add(addEnemy.getObject());
  }

  @Subscribe
  void removeObject(RemoveObject removeObject) {
    worldView.remove(removeObject.getObject());
  }
}
