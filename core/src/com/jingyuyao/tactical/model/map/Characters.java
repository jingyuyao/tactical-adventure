package com.jingyuyao.tactical.model.map;

import com.google.common.collect.FluentIterable;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.AddEnemy;
import com.jingyuyao.tactical.model.event.AddPlayer;
import com.jingyuyao.tactical.model.event.RemoveObject;
import com.jingyuyao.tactical.model.map.MapModule.BackingCharacterSet;
import java.util.Iterator;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * A concrete singleton type that holds all the {@link Character} on the map.
 */
@Singleton
public class Characters {

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

  public void removeDead() {
    for (Iterator<Character> iterator = characterSet.iterator(); iterator.hasNext(); ) {
      Character character = iterator.next();
      if (character.getHp() == 0) {
        iterator.remove();
        eventBus.post(new RemoveObject(character));
      }
    }
  }

  public FluentIterable<Character> fluent() {
    return FluentIterable.from(characterSet);
  }
}
