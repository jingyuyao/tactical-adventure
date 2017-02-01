package com.jingyuyao.tactical.model.item;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Marker;
import com.jingyuyao.tactical.model.map.Marking;
import com.jingyuyao.tactical.model.map.Marking.MarkingBuilder;
import com.jingyuyao.tactical.model.map.Terrains;
import com.jingyuyao.tactical.model.terrain.Terrain;
import javax.inject.Inject;

// TODO: need a method that return a "target info" for this target to be displayed
public class Target {

  private final ImmutableSet<Coordinate> selectCoordinates;
  private final ImmutableSet<Coordinate> targetCoordinates;
  private final Characters characters;
  private final Terrains terrains;
  private Marking marking;

  @Inject
  Target(
      @Assisted("select") Iterable<Coordinate> selectCoordinates,
      @Assisted("target") Iterable<Coordinate> targetCoordinates,
      Characters characters,
      Terrains terrains) {
    this.selectCoordinates = ImmutableSet.copyOf(selectCoordinates);
    this.targetCoordinates = ImmutableSet.copyOf(targetCoordinates);
    this.characters = characters;
    this.terrains = terrains;
  }

  /**
   * Returns whether or not {@code coordinate} "selects" this {@link Target}.
   */
  public boolean selectedBy(Coordinate coordinate) {
    return selectCoordinates.contains(coordinate);
  }

  public boolean canTarget(Coordinate coordinate) {
    return targetCoordinates.contains(coordinate);
  }

  /**
   * @return a current view of all the characters being targeted
   */
  public ImmutableSet<Character> getTargetCharacters() {
    // Returns an immutable set since characters can die and we don't want an iteration error
    return ImmutableSet.copyOf(characters.fluent().filter(new Predicate<Character>() {
      @Override
      public boolean apply(Character input) {
        return targetCoordinates.contains(input.getCoordinate());
      }
    }));
  }

  public void showMarking() {
    marking = createMarking();
    marking.apply();
  }

  public void hideMarking() {
    marking.clear();
    marking = null;
  }

  public Marking createHitMarking() {
    MarkingBuilder builder = new MarkingBuilder();
    Iterable<MapObject> hitObjects =
        Iterables.<MapObject>concat(
            terrains.getAll(targetCoordinates),
            getTargetCharacters());
    for (MapObject object : hitObjects) {
      builder.put(object, Marker.HIT);
    }
    return builder.build();
  }

  private Marking createMarking() {
    MarkingBuilder builder = new MarkingBuilder();
    for (Terrain terrain : terrains.getAll(selectCoordinates)) {
      builder.put(terrain, Marker.TARGET_SELECT);
    }
    for (Terrain terrain : terrains.getAll(targetCoordinates)) {
      builder.put(terrain, Marker.CAN_ATTACK);
    }
    for (Character character : getTargetCharacters()) {
      builder.put(character, Marker.POTENTIAL_TARGET);
    }
    return builder.build();
  }
}
