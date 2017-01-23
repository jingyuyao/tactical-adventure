package com.jingyuyao.tactical.model.item;

import com.google.common.collect.ImmutableSet;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.map.Terrains;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.mark.Marking;
import com.jingyuyao.tactical.model.mark.MarkingFactory;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

// TODO: need a method that return a "target info" for this target to be displayed
public class Target {

  private final ImmutableSet<Coordinate> selectCoordinates;
  private final ImmutableSet<Coordinate> targetCoordinates;
  private final Characters characters;
  private final Terrains terrains;
  private final MarkingFactory markingFactory;
  private Marking marking;

  @Inject
  Target(
      @Assisted("select") Iterable<Coordinate> selectCoordinates,
      @Assisted("target") Iterable<Coordinate> targetCoordinates,
      Characters characters,
      Terrains terrains,
      MarkingFactory markingFactory) {
    this.selectCoordinates = ImmutableSet.copyOf(selectCoordinates);
    this.targetCoordinates = ImmutableSet.copyOf(targetCoordinates);
    this.characters = characters;
    this.terrains = terrains;
    this.markingFactory = markingFactory;
  }

  /**
   * Returns whether or not {@code coordinate} "selects" this {@link Target}.
   */
  public boolean selectedBy(Coordinate coordinate) {
    return selectCoordinates.contains(coordinate);
  }

  /**
   * @return a current view of all the characters being targeted
   */
  public ImmutableSet<Character> getTargetCharacters() {
    // Returns an immutable set since characters can die and we don't want an iteration error
    return ImmutableSet.copyOf(characters.getAll(targetCoordinates));
  }

  public void showMarking() {
    marking = createMarking();
    marking.apply();
  }

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
    return markingFactory.create(markerMap);
  }
}
