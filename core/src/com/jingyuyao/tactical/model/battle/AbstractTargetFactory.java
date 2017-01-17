package com.jingyuyao.tactical.model.battle;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.common.Directions;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.map.Terrains;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.mark.Marking;
import java.util.HashMap;
import java.util.Map;

abstract class AbstractTargetFactory implements TargetFactory {

  private final Characters characters;
  private final Terrains terrains;

  AbstractTargetFactory(Characters characters, Terrains terrains) {
    this.characters = characters;
    this.terrains = terrains;
  }

  @Override
  public ImmutableList<Target> create(Character attacker, Weapon weapon) {
    ImmutableList.Builder<Target> builder = ImmutableList.builder();
    for (Coordinate direction : Directions.ALL) {
      Optional<Target> targetOptional = create(attacker, weapon, direction);
      if (targetOptional.isPresent()) {
        builder.add(targetOptional.get());
      }
    }
    return builder.build();
  }

  abstract Optional<Target> create(Character attacker, Weapon weapon, Coordinate direction);

  Characters getCharacters() {
    return characters;
  }

  Terrains getTerrains() {
    return terrains;
  }

  Marking createMarking(
      Iterable<Coordinate> targetCoordinates, Iterable<Character> targetCharacters) {
    Map<MapObject, Marker> markerMap = new HashMap<MapObject, Marker>();
    for (Terrain terrain : terrains.getAll(targetCoordinates)) {
      markerMap.put(terrain, Marker.CAN_ATTACK);
    }
    for (Character character : targetCharacters) {
      markerMap.put(character, Marker.POTENTIAL_TARGET);
    }
    return new Marking(markerMap);
  }
}
