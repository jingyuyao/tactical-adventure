package com.jingyuyao.tactical.model.mark;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.inject.BindingAnnotation;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Waiter;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Targets;
import com.jingyuyao.tactical.model.map.Terrain;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.Set;
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
  private final Provider<Map<MapObject, Marker>> markerMapProvider;

  @Inject
  MarkingFactory(
      EventBus eventBus,
      Waiter waiter,
      @InitialMarkerMap Provider<Map<MapObject, Marker>> markerMapProvider) {
    this.eventBus = eventBus;
    this.waiter = waiter;
    this.markerMapProvider = markerMapProvider;
  }

  public Marking create(Character owner, Map<MapObject, Marker> markerMap) {
    return new Marking(eventBus, owner, markerMap, waiter);
  }

  public Marking allTargetsWithMove(Targets targets) {
    ImmutableSet<Terrain> canMoveToTerrains = ImmutableSet.copyOf(targets.moveTerrains());
    ImmutableSet<Terrain> canAttackTerrains = ImmutableSet.copyOf(targets.all().terrains());
    Set<Terrain> canAttackTerrainsToShow = Sets.difference(canAttackTerrains, canMoveToTerrains);
    Iterable<Character> canTargetCharacters = targets.all().characters();

    return this.new Builder(targets.getCharacter())
        .add(canMoveToTerrains, Marker.CAN_MOVE_TO)
        .add(canAttackTerrainsToShow, Marker.CAN_ATTACK)
        .add(canTargetCharacters, Marker.POTENTIAL_TARGET)
        .build();
  }

  public Marking immediateTargets(Targets targets) {
    Iterable<Terrain> canAttackTerrains = targets.immediate().terrains();
    Iterable<Character> canTargetCharacters = targets.immediate().characters();

    return this.new Builder(targets.getCharacter())
        .add(canAttackTerrains, Marker.CAN_ATTACK)
        .add(canTargetCharacters, Marker.POTENTIAL_TARGET)
        .build();
  }

  public Marking immediateTargetsWithChosenCharacter(Targets targets, Character chosen) {
    Iterable<Terrain> canAttackTerrains = targets.immediate().terrains();

    return this.new Builder(targets.getCharacter())
        .add(canAttackTerrains, Marker.CAN_ATTACK)
        .add(chosen, Marker.CHOSEN_TARGET)
        .build();
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
