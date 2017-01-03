package com.jingyuyao.tactical.model.map;

import com.google.common.collect.Iterables;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.BindingAnnotation;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.character.event.CharacterDied;
import com.jingyuyao.tactical.model.common.Disposable;
import com.jingyuyao.tactical.model.common.EventObject;
import com.jingyuyao.tactical.model.state.Waiting;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Iterator;
import java.util.Set;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * A container for a set of {@link Character}s.
 */
// TODO: consider making this a Glazed List?
@Singleton
public class CharacterContainer extends EventObject implements Iterable<Character>, Disposable {
    private final Set<Character> characters;

    @Inject
    CharacterContainer(EventBus eventBus, @InitialCharacterSet Set<Character> characters) {
        super(eventBus);
        this.characters = characters;
        register();
    }

    @Override
    public void dispose() {
        for (Character character : characters) {
            character.dispose();
        }
        characters.clear();
    }

    @Subscribe
    public void characterDied(CharacterDied characterDied) {
        characters.remove(characterDied.getObject());
    }

    @Subscribe
    public void endTurn(Waiting.EndTurn endTurn) {
        for (Player player : getPlayers()) {
            player.setActionable(true);
        }
    }

    public void add(Character character) {
        characters.add(character);
    }

    public void addAll(Iterable<? extends Character> characters) {
        Iterables.addAll(this.characters, characters);
    }

    public Iterable<Player> getPlayers() {
        return Iterables.filter(characters, Player.class);
    }

    public Iterable<Enemy> getEnemies() {
        return Iterables.filter(characters, Enemy.class);
    }

    @Override
    public Iterator<Character> iterator() {
        return characters.iterator();
    }

    @BindingAnnotation @Target({FIELD, PARAMETER, METHOD}) @Retention(RUNTIME)
    @interface InitialCharacterSet {}
}
