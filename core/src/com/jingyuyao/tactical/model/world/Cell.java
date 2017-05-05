package com.jingyuyao.tactical.model.world;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.InstantMoveCharacter;
import com.jingyuyao.tactical.model.event.MoveCharacter;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.RemoveCharacter;
import com.jingyuyao.tactical.model.event.SpawnCharacter;
import com.jingyuyao.tactical.model.terrain.Terrain;
import javax.inject.Inject;

public class Cell {

  private final ModelBus modelBus;
  private final Coordinate coordinate;
  private final Terrain terrain;
  private Character character;

  @Inject
  Cell(ModelBus modelBus, @Assisted Coordinate coordinate, @Assisted Terrain terrain) {
    this.modelBus = modelBus;
    this.coordinate = coordinate;
    this.terrain = terrain;
  }

  public Coordinate getCoordinate() {
    return coordinate;
  }

  public Terrain getTerrain() {
    return terrain;
  }

  public Optional<Character> character() {
    return Optional.fromNullable(character);
  }

  public Optional<Player> player() {
    if (character != null && character instanceof Player) {
      return Optional.of((Player) character);
    }
    return Optional.absent();
  }

  public Optional<Enemy> enemy() {
    if (character != null && character instanceof Enemy) {
      return Optional.of((Enemy) character);
    }
    return Optional.absent();
  }

  public void spawnCharacter(Character character) {
    Preconditions.checkState(this.character == null);
    Preconditions.checkNotNull(character);

    this.character = character;
    modelBus.post(new SpawnCharacter(this));
  }

  public void removeCharacter() {
    Preconditions.checkState(character != null);

    modelBus.post(new RemoveCharacter(character));
    this.character = null;
  }

  public void instantMoveCharacter(Cell destination) {
    Preconditions.checkState(character != null);

    if (destination.equals(this)) {
      return;
    }

    Preconditions.checkArgument(destination.character == null);

    modelBus.post(new InstantMoveCharacter(character, destination));
    destination.character = character;
    character = null;
  }

  public Promise moveCharacter(Path path) {
    Preconditions.checkState(character != null);
    Preconditions.checkArgument(path.getOrigin().equals(this));

    Cell destination = path.getDestination();
    if (destination.equals(this)) {
      return Promise.immediate();
    }

    Preconditions.checkArgument(destination.character == null);

    Promise promise = new Promise();
    modelBus.post(new MoveCharacter(character, path, promise));
    path.getDestination().character = character;
    character = null;
    return promise;
  }
}
