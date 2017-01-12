package com.jingyuyao.tactical.model.character;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.event.InstantMove;
import com.jingyuyao.tactical.model.character.event.Move;
import com.jingyuyao.tactical.model.character.event.RemoveCharacter;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.common.Disposable;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Path;
import com.jingyuyao.tactical.model.map.Targets;
import com.jingyuyao.tactical.model.map.TargetsFactory;
import com.jingyuyao.tactical.model.map.Terrain;
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

  /**
   * Return the current {@link Targets} for this {@link Character}.
   */
  public Targets createTargets() {
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

  public int getMoveDistance() {
    return stats.getMoveDistance();
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
      post(new RemoveCharacter(this));
      dispose();
    }
  }

  public void healBy(int delta) {
    stats.healBy(delta);
  }

  /**
   * "Hit" {@code other} using the currently equipped weapon if possible.
   */
  public void tryHit(Character other) {
    // Hum... we could have a special after death attack!
    if (stats.getHp() == 0) {
      return;
    }

    Optional<Weapon> weaponOptional = items.getEquippedWeapon();
    if (!weaponOptional.isPresent()) {
      return;
    }

    Weapon weapon = weaponOptional.get();
    if (weapon.canTarget(this, other)) {
      weapon.hit(other);
    }
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("name", name).toString() + super.toString();
  }
}
