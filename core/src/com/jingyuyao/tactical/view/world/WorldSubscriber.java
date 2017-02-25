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

  private final World world;

  @Inject
  WorldSubscriber(World world) {
    this.world = world;
  }

  @Subscribe
  public void addTerrain(AddTerrain addTerrain) {
    world.add(addTerrain.getObject());
  }

  @Subscribe
  public void addPlayer(AddPlayer addPlayer) {
    world.add(addPlayer.getObject());
  }

  @Subscribe
  public void addEnemy(AddEnemy addEnemy) {
    world.add(addEnemy.getObject());
  }

  @Subscribe
  public void removeObject(RemoveObject removeObject) {
    world.remove(removeObject.getObject());
  }
}
