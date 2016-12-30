package com.jingyuyao.tactical.model;

import com.google.common.collect.Iterables;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.BindingAnnotation;
import com.jingyuyao.tactical.model.event.NewTurn;
import com.jingyuyao.tactical.model.object.Character;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.util.DisposableObject;
import com.jingyuyao.tactical.model.util.Disposed;

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
 * Listens for {@link #dispose()} in its contained {@link #characters} and remove them from the set.
 * This also implicitly listens for {@link com.jingyuyao.tactical.model.util.ResetModel} via its parent class.
 */
// TODO: consider making this a Glazed List?
@Singleton
public class CharacterContainer extends DisposableObject implements Iterable<Character> {
    private final Set<Character> characters;

    @Inject
    CharacterContainer(EventBus eventBus, @InitialCharacterSet Set<Character> characters) {
        super(eventBus);
        this.characters = characters;
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

    @Subscribe
    public void characterDisposed(Disposed<Character> disposed) {
        characters.remove(disposed.getObject());
    }

    @Subscribe
    public void newTurn(NewTurn newTurn) {
        for (Player player : getPlayers()) {
            player.setActionable(true);
        }
    }

    @Override
    protected void disposed() {
        characters.clear();
        super.disposed();
    }

    @Override
    public Iterator<Character> iterator() {
        return characters.iterator();
    }

    @BindingAnnotation @Target({FIELD, PARAMETER, METHOD}) @Retention(RUNTIME)
    @interface InitialCharacterSet {}
}
