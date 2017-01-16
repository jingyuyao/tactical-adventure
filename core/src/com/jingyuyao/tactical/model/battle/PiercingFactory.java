package com.jingyuyao.tactical.model.battle;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
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
import javax.inject.Inject;
import javax.inject.Singleton;

// TODO: test me
@Singleton
public class PiercingFactory {

  private final Characters characters;
  private final Terrains terrains;

  @Inject
  PiercingFactory(Characters characters, Terrains terrains) {
    this.characters = characters;
    this.terrains = terrains;
  }

  public Optional<Target> create(Weapon weapon, Coordinate origin, Coordinate direction) {
    ImmutableSet.Builder<Coordinate> targetBuilder = ImmutableSet.builder();
    Coordinate select = origin.offsetBy(direction);
    Coordinate current = select.offsetBy(direction);
    while (terrains.contains(current)) {
      targetBuilder.add(current);
      current = current.offsetBy(direction);
    }

    final ImmutableSet<Coordinate> targetCoordinates = targetBuilder.build();
    if (targetCoordinates.isEmpty()) {
      return Optional.absent();
    }

    ImmutableList<Character> targetCharacters = ImmutableList.copyOf(
        Iterables.filter(characters, new Predicate<Character>() {
          @Override
          public boolean apply(Character input) {
            return targetCoordinates.contains(input.getCoordinate());
          }
        })
    );
    Terrain selectTerrain = terrains.get(select);
    ImmutableSet<Terrain> targetTerrains = ImmutableSet.copyOf(terrains.getAll(targetCoordinates));

    Map<MapObject, Marker> markerMap = new HashMap<MapObject, Marker>();
    markerMap.put(selectTerrain, Marker.DANGER);
    for (Terrain terrain : targetTerrains) {
      markerMap.put(terrain, Marker.CAN_ATTACK);
    }
    for (Character character : targetCharacters) {
      markerMap.put(character, Marker.POTENTIAL_TARGET);
    }
    Marking marking = new Marking(markerMap);

    return Optional.<Target>of(new ConstantDamage(weapon, select, targetCharacters, marking));
  }
}
