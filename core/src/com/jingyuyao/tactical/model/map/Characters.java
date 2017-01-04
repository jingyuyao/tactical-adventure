package com.jingyuyao.tactical.model.map;

import com.google.common.collect.Iterables;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.BindingAnnotation;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.event.CharacterDied;
import com.jingyuyao.tactical.model.common.EventBusObject;
import com.jingyuyao.tactical.model.common.ManagedBy;
import com.jingyuyao.tactical.model.event.ClearMap;
import com.jingyuyao.tactical.model.event.NewMap;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Iterator;
import java.util.Set;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/** A concrete singleton type that holds all the {@link Character} on the map. */
@Singleton
public class Characters extends EventBusObject
    implements ManagedBy<NewMap, ClearMap>, Iterable<Character> {
  private final Set<Character> characters;

  @Inject
  Characters(EventBus eventBus, @BackingCharacterSet Set<Character> characters) {
    super(eventBus);
    this.characters = characters;
    register();
  }

  @Subscribe
  @Override
  public void initialize(NewMap data) {
    Iterables.addAll(characters, Iterables.concat(data.getPlayers(), data.getEnemies()));
  }

  @Subscribe
  @Override
  public void dispose(ClearMap clearMap) {
    for (Character character : characters) {
      character.dispose();
    }
    characters.clear();
  }

  @Subscribe
  public void characterDied(CharacterDied characterDied) {
    characters.remove(characterDied.getObject());
  }

  @Override
  public Iterator<Character> iterator() {
    return characters.iterator();
  }

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface BackingCharacterSet {}
}
