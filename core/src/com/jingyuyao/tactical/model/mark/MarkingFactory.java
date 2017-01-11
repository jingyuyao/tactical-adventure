package com.jingyuyao.tactical.model.mark;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.inject.BindingAnnotation;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Waiter;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Targets;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.map.Terrains;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * Produces {@link Marking}s
 */
@Singleton
public class MarkingFactory {

  private final EventBus eventBus;
  private final Waiter waiter;
  private final Terrains terrains;
  private final Provider<Map<MapObject, Marker>> markerMapProvider;

  @Inject
  MarkingFactory(
      EventBus eventBus,
      Waiter waiter,
      Terrains terrains,
      @InitialMarkerMap Provider<Map<MapObject, Marker>> markerMapProvider) {
    this.eventBus = eventBus;
    this.waiter = waiter;
    this.terrains = terrains;
    this.markerMapProvider = markerMapProvider;
  }

  public Marking allTargetsWithMove(Targets targets) {
    Iterable<Terrain> canMoveToTerrains = terrains.getAll(targets.moveCoordinates());
    Iterable<Terrain> canAttackTerrains =
        terrains.getAll(Sets.difference(targets.all().coordinates(), targets.moveCoordinates()));
    Iterable<Character> canTargetCharacters = targets.all().characters();

    return this.new Builder(targets.getCharacter())
        .add(canMoveToTerrains, Marker.CAN_MOVE_TO)
        .add(canAttackTerrains, Marker.CAN_ATTACK)
        .add(canTargetCharacters, Marker.POTENTIAL_TARGET)
        .build();
  }

  public Marking immediateTargets(Targets targets) {
    Iterable<Terrain> canAttackTerrains = terrains.getAll(targets.immediate().coordinates());
    Iterable<Character> canTargetCharacters = targets.immediate().characters();

    return this.new Builder(targets.getCharacter())
        .add(canAttackTerrains, Marker.CAN_ATTACK)
        .add(canTargetCharacters, Marker.POTENTIAL_TARGET)
        .build();
  }

  public Marking immediateTargetsWithChosenCharacter(Targets targets, Character chosen) {
    Iterable<Terrain> canAttackTerrains = terrains.getAll(targets.immediate().coordinates());
    // TODO: fix me
    List<Character> canTargetCharacters = Lists.newArrayList(targets.immediate().characters());
    canTargetCharacters.remove(chosen);

    return this.new Builder(targets.getCharacter())
        .add(canAttackTerrains, Marker.CAN_ATTACK)
        .add(canTargetCharacters, Marker.POTENTIAL_TARGET)
        .add(chosen, Marker.CHOSEN_TARGET)
        .build();
  }

  public Marking danger(Targets targets) {
    Iterable<Terrain> allTargets = terrains.getAll(targets.all().coordinates());

    return this.new Builder(targets.getCharacter()).add(allTargets, Marker.DANGER).build();
  }

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface InitialMarkerMap {

  }

  private class Builder {

    private final Character character;
    private final java.util.Map<MapObject, Marker> markerMap;

    private Builder(Character character) {
      this.character = character;
      markerMap = markerMapProvider.get();
    }

    private Builder add(MapObject object, Marker marker) {
      markerMap.put(object, marker);
      return this;
    }

    private Builder add(Iterable<? extends MapObject> objects, Marker marker) {
      for (MapObject object : objects) {
        markerMap.put(object, marker);
      }
      return this;
    }

    private Marking build() {
      return new Marking(eventBus, character, markerMap, waiter);
    }
  }
}
