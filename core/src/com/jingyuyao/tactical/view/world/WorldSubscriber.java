package com.jingyuyao.tactical.view.world;

import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.event.InstantMoveCharacter;
import com.jingyuyao.tactical.model.event.MoveCharacter;
import com.jingyuyao.tactical.model.event.RemoveCharacter;
import com.jingyuyao.tactical.model.event.SpawnCharacter;
import com.jingyuyao.tactical.model.event.WorldLoad;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.view.actor.CharacterActor;
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
      worldView.add(cell.getCoordinate(), cell.getTerrain());
    }
  }

  @Subscribe
  void worldReset(WorldReset worldReset) {
    worldView.reset();
  }

  @Subscribe
  void spawnCharacter(SpawnCharacter spawnCharacter) {
    Cell cell = spawnCharacter.getObject();
    Coordinate coordinate = cell.getCoordinate();
    if (cell.hasPlayer()) {
      worldView.add(coordinate, cell.getPlayer());
    } else if (cell.hasEnemy()) {
      worldView.add(coordinate, cell.getEnemy());
    }
  }

  @Subscribe
  void removeCharacter(RemoveCharacter removeCharacter) {
    worldView.remove(removeCharacter.getObject());
  }

  @Subscribe
  void instantMoveCharacter(InstantMoveCharacter instantMoveCharacter) {
    CharacterActor actor = worldView.get(instantMoveCharacter.getCharacter());
    actor.moveTo(instantMoveCharacter.getDestination().getCoordinate());
  }

  @Subscribe
  void moveCharacter(MoveCharacter moveCharacter) {
    CharacterActor actor = worldView.get(moveCharacter.getCharacter());
    actor.moveAlong(moveCharacter.getPath(), moveCharacter.getFuture());
  }
}
