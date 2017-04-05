package com.jingyuyao.tactical.view.world2;

import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.event.InstantMoveCharacter;
import com.jingyuyao.tactical.model.event.MoveCharacter;
import com.jingyuyao.tactical.model.event.RemoveCharacter;
import com.jingyuyao.tactical.model.event.SpawnCharacter;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldSubscriber {

  private final Entities entities;
  private final CharacterEntities characterEntities;

  @Inject
  WorldSubscriber(
      Entities entities,
      CharacterEntities characterEntities) {
    this.entities = entities;
    this.characterEntities = characterEntities;
  }

  @Subscribe
  void worldReset(WorldReset worldReset) {
    entities.reset();
  }

  @Subscribe
  void spawnCharacter(SpawnCharacter spawnCharacter) {
    Cell cell = spawnCharacter.getObject();
    Coordinate coordinate = cell.getCoordinate();
    if (cell.hasPlayer()) {
      characterEntities.add(coordinate, cell.getPlayer());
    } else if (cell.hasEnemy()) {
      characterEntities.add(coordinate, cell.getEnemy());
    }
  }

  @Subscribe
  void removeCharacter(RemoveCharacter removeCharacter) {
    characterEntities.remove(removeCharacter.getObject());
  }

  @Subscribe
  void instantMoveCharacter(InstantMoveCharacter instantMoveCharacter) {
//    CharacterActor actor = worldView.get(instantMoveCharacter.getCharacter());
//    actor.moveTo(instantMoveCharacter.getDestination().getCoordinate());
  }

  @Subscribe
  void moveCharacter(MoveCharacter moveCharacter) {
//    CharacterActor actor = worldView.get(moveCharacter.getCharacter());
//    actor.moveAlong(moveCharacter.getPath(), moveCharacter.getFuture());
  }
}
