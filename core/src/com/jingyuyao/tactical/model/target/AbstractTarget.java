package com.jingyuyao.tactical.model.target;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.map.Terrains;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.mark.Marking;
import java.util.HashMap;
import java.util.Map;

abstract class AbstractTarget implements Target {

  private final Character attacker;
  private final Weapon weapon;
  private final ImmutableSet<Coordinate> selectCoordinates;
  private final ImmutableSet<Coordinate> targetCoordinates;
  private final Characters characters;
  private final Terrains terrains;
  private Marking marking;

  AbstractTarget(
      Character attacker,
      Weapon weapon,
      ImmutableSet<Coordinate> selectCoordinates,
      ImmutableSet<Coordinate> targetCoordinates,
      Characters characters,
      Terrains terrains) {
    this.attacker = attacker;
    this.weapon = weapon;
    this.selectCoordinates = selectCoordinates;
    this.targetCoordinates = targetCoordinates;
    this.characters = characters;
    this.terrains = terrains;
  }

  Character getAttacker() {
    return attacker;
  }

  Weapon getWeapon() {
    return weapon;
  }

  @Override
  public ImmutableSet<MapObject> getSelectObjects() {
    return ImmutableSet.copyOf(Iterables.concat(
        terrains.getAll(selectCoordinates),
        characters.getAll(selectCoordinates)
    ));
  }

  @Override
  public ImmutableSet<Character> getTargetCharacters() {
    return ImmutableSet.copyOf(characters.getAll(targetCoordinates));
  }

  @Override
  public void showMarking() {
    marking = createMarking();
    marking.apply();
  }

  @Override
  public void hideMarking() {
    marking.clear();
    marking = null;
  }

  private Marking createMarking() {
    Map<MapObject, Marker> markerMap = new HashMap<MapObject, Marker>();
    for (Terrain terrain : terrains.getAll(targetCoordinates)) {
      markerMap.put(terrain, Marker.CAN_ATTACK);
    }
    for (Character character : getTargetCharacters()) {
      markerMap.put(character, Marker.POTENTIAL_TARGET);
    }
    return new Marking(markerMap);
  }
}
