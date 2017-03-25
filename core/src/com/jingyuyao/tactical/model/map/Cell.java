package com.jingyuyao.tactical.model.map;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.event.InstantMoveCharacter;
import com.jingyuyao.tactical.model.map.event.MoveCharacter;
import com.jingyuyao.tactical.model.map.event.RemoveCharacter;
import com.jingyuyao.tactical.model.map.event.SpawnCharacter;
import com.jingyuyao.tactical.model.terrain.Terrain;
import javax.inject.Inject;

public class Cell {

  private final EventBus eventBus;
  private final Coordinate coordinate;
  private final Terrain terrain;
  private Character character;

  @Inject
  Cell(
      @ModelEventBus EventBus eventBus,
      @Assisted Coordinate coordinate,
      @Assisted Terrain terrain) {
    this.eventBus = eventBus;
    this.coordinate = coordinate;
    this.terrain = terrain;
  }

  public Coordinate getCoordinate() {
    return coordinate;
  }

  public Terrain getTerrain() {
    return terrain;
  }

  public boolean hasCharacter() {
    return character != null;
  }

  public Character getCharacter() {
    return character;
  }

  public boolean hasPlayer() {
    return hasCharacter() && character instanceof Player;
  }

  public Player getPlayer() {
    return (Player) character;
  }

  public boolean hasEnemy() {
    return hasCharacter() && character instanceof Enemy;
  }

  public Enemy getEnemy() {
    return (Enemy) character;
  }

  public void spawnCharacter(Character character) {
    Preconditions.checkState(!hasCharacter());
    Preconditions.checkNotNull(character);

    eventBus.post(new SpawnCharacter(character));
    this.character = character;
  }

  public void removeCharacter() {
    Preconditions.checkState(hasCharacter());

    eventBus.post(new RemoveCharacter(character));
    this.character = null;
  }

  public void instantMoveCharacter(Cell cell) {
    Preconditions.checkState(hasCharacter());
    Preconditions.checkArgument(!cell.hasCharacter());

    eventBus.post(new InstantMoveCharacter(character, cell));
    cell.character = character;
    character = null;
  }

  public ListenableFuture<Void> moveCharacter(Path path) {
    Preconditions.checkState(hasCharacter());
    Preconditions.checkArgument(path.getOrigin().equals(this));
    Preconditions.checkArgument(!path.getDestination().hasCharacter());

    SettableFuture<Void> future = SettableFuture.create();
    eventBus.post(new MoveCharacter(character, path, future));
    path.getDestination().character = character;
    character = null;
    return future;
  }
}
