package com.jingyuyao.tactical.model.item;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.mark.Marking;
import com.jingyuyao.tactical.model.target.Target;
import java.util.HashMap;
import java.util.Map;

/**
 * An {@link Item} that can affect a {@link Character}'s HP and status. Not a {@link Consumable}
 * since the effect of a {@link Weapon} depends on its user.
 */
public abstract class Weapon extends Usable {

  private final int attackPower;

  Weapon(
      EventBus eventBus,
      @Assisted String name,
      @Assisted("usageLeft") int usageLeft,
      @Assisted("attackPower") int attackPower) {
    super(eventBus, name, usageLeft);
    this.attackPower = attackPower;
  }

  public int getAttackPower() {
    return attackPower;
  }

  public abstract ImmutableList<Target> createTargets(Character attacker);

  Marking createMarking(Iterable<Terrain> targetTerrains, Iterable<Character> targetCharacters) {
    Map<MapObject, Marker> markerMap = new HashMap<MapObject, Marker>();
    for (Terrain terrain : targetTerrains) {
      markerMap.put(terrain, Marker.CAN_ATTACK);
    }
    for (Character character : targetCharacters) {
      markerMap.put(character, Marker.POTENTIAL_TARGET);
    }
    return new Marking(markerMap);
  }
}
