package com.jingyuyao.tactical.model.character;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.character.event.CharacterDied;
import com.jingyuyao.tactical.model.character.event.InstantMove;
import com.jingyuyao.tactical.model.character.event.Move;
import com.jingyuyao.tactical.model.common.Disposable;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.*;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.state.MapState;

import java.util.List;

public abstract class Character extends MapObject implements Disposable {
  private final String name;
  private final Stats stats;
  private final Items items;
  private final TargetsFactory targetsFactory;

  Character(
      EventBus eventBus,
      Coordinate coordinate,
      List<Marker> markers,
      String name,
      Stats stats,
      Items items,
      TargetsFactory targetsFactory) {
    super(eventBus, coordinate, markers);
    this.targetsFactory = targetsFactory;
    this.name = name;
    this.stats = stats;
    this.items = items;
  }

  @Override
  public void dispose() {
    items.dispose();
  }

  @Override
  public void highlight(MapState mapState) {
    mapState.highlight(this);
  }

  /** Return the current target info for this {@link Character}. */
  public Targets createTargetInfo() {
    return targetsFactory.create(this);
  }

  public String getName() {
    return name;
  }

  public int getHp() {
    return stats.getHp();
  }

  public Iterable<Consumable> getConsumables() {
    return items.getConsumables();
  }

  public Iterable<Weapon> getWeapons() {
    return items.getWeapons();
  }

  public Optional<Weapon> getEquippedWeapon() {
    return items.getEquippedWeapon();
  }

  public int getMoveDistance() {
    return stats.getMoveDistance();
  }

  public boolean isAlive() {
    return stats.getHp() > 0;
  }

  public boolean canTarget(Character other) {
    // TODO: make me more specific later
    return !Objects.equal(this, other) && !Objects.equal(getClass(), other.getClass());
  }

  public boolean canPassTerrainType(Terrain.Type terrainType) {
    return stats.canPassTerrainType(terrainType);
  }

  public void move(Path path) {
    setCoordinate(path.getDestination());
    post(new Move(this, path));
  }

  public void instantMoveTo(Coordinate newCoordinate) {
    setCoordinate(newCoordinate);
    post(new InstantMove(this, newCoordinate));
  }

  public void equipWeapon(Weapon weapon) {
    items.setEquippedWeapon(weapon);
  }

  public void damageBy(int delta) {
    boolean dead = stats.damageBy(delta);
    if (dead) {
      post(new CharacterDied(this));
      dispose();
    }
  }

  public void healBy(int delta) {
    stats.healBy(delta);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("name", name).toString() + super.toString();
  }
}
