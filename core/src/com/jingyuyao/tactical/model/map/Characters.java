package com.jingyuyao.tactical.model.map;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.EventBus;
import com.google.inject.BindingAnnotation;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.event.AddEnemy;
import com.jingyuyao.tactical.model.event.AddPlayer;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Iterator;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * A concrete singleton type that holds all the {@link Character} on the map.
 */
@Singleton
public class Characters implements Iterable<Character> {

  private final EventBus eventBus;
  private final Set<Character> characterSet;

  @Inject
  Characters(@ModelEventBus EventBus eventBus, @BackingCharacterSet Set<Character> characterSet) {
    this.eventBus = eventBus;
    this.characterSet = characterSet;
  }

  public void add(Player player) {
    characterSet.add(player);
    eventBus.post(new AddPlayer(player));
  }

  public void add(Enemy enemy) {
    characterSet.add(enemy);
    eventBus.post(new AddEnemy(enemy));
  }

  public void removeCharacter(Character character) {
    characterSet.remove(character);
  }

  /**
   * Return a snapshot of the {@link Coordinate}s in {@link #characterSet}.
   */
  public ImmutableSet<Coordinate> coordinates() {
    ImmutableSet.Builder<Coordinate> builder = new Builder<Coordinate>();
    for (Character character : characterSet) {
      builder.add(character.getCoordinate());
    }
    return builder.build();
  }

  public Iterable<Character> getAll(final ImmutableSet<Coordinate> coordinates) {
    return Iterables.filter(characterSet, new Predicate<Character>() {
      @Override
      public boolean apply(Character input) {
        return coordinates.contains(input.getCoordinate());
      }
    });
  }

  @Override
  public Iterator<Character> iterator() {
    return characterSet.iterator();
  }

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface BackingCharacterSet {

  }
}
