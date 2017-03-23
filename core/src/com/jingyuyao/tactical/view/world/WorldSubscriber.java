package com.jingyuyao.tactical.view.world;

import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.event.RemoveObject;
import com.jingyuyao.tactical.model.event.WorldLoad;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.model.map.Cell;
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
  void worldLoad(WorldLoad worldLoad) {
    for (Cell cell : worldLoad.getObject()) {
      worldView.add(cell);
    }
  }

  @Subscribe
  void worldReset(WorldReset worldReset) {
    worldView.reset();
  }

  @Subscribe
  void removeObject(RemoveObject removeObject) {
    worldView.remove(removeObject.getObject());
  }
}
