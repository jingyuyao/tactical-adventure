package com.jingyuyao.tactical.model.mark;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.common.eventbus.EventBus;
import com.google.inject.BindingAnnotation;
import com.jingyuyao.tactical.model.Waiter;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Targets;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.map.Terrains;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
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
  public MarkingFactory(
      EventBus eventBus,
      Waiter waiter,
      Terrains terrains,
      @InitialMarkerMap Provider<Map<MapObject, Marker>> markerMapProvider) {
    this.eventBus = eventBus;
    this.waiter = waiter;
    this.terrains = terrains;
    this.markerMapProvider = markerMapProvider;
  }

  public Marking moveAndTargets(Targets targets) {
    Iterable<Terrain> canMoveToTerrains = terrains.getAll(targets.moves());
    Iterable<Terrain> canAttackTerrains = terrains.getAll(targets.allTargetsMinusMove());
    Iterable<Character> canTargetCharacters = targets.allTargetCharacters();

    return this.new Builder(targets.getCharacter())
        .add(canMoveToTerrains, Marker.CAN_MOVE_TO)
        .add(canAttackTerrains, Marker.CAN_ATTACK)
        .add(canTargetCharacters, Marker.POTENTIAL_TARGET)
        .build();
  }

  public Marking immediateTargets(Targets targets) {
    Iterable<Terrain> canAttackTerrains = terrains.getAll(targets.immediateTargets());
    Iterable<Character> canTargetCharacters = targets.immediateTargetCharacters();

    return this.new Builder(targets.getCharacter())
        .add(canAttackTerrains, Marker.CAN_ATTACK)
        .add(canTargetCharacters, Marker.POTENTIAL_TARGET)
        .build();
  }

  public Marking danger(Targets targets) {
    Iterable<Terrain> allTargets = terrains.getAll(targets.allTargets());

    return this.new Builder(targets.getCharacter()).add(allTargets, Marker.DANGER).build();
  }

  private class Builder {

    private final Character character;
    private final java.util.Map<MapObject, Marker> markerMap;

    private Builder(Character character) {
      this.character = character;
      markerMap = markerMapProvider.get();
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

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface InitialMarkerMap {

  }
}
