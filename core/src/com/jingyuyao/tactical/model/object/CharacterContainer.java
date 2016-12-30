package com.jingyuyao.tactical.model.object;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.util.DisposableObject;
import com.jingyuyao.tactical.model.util.Disposed;

import java.util.Iterator;
import java.util.Set;

/**
 * A container for a set of {@link Character}s.
 * Listens for {@link #dispose()} in its contained {@link #objects} and remove them from the set.
 * This also implicitly listens for {@link com.jingyuyao.tactical.model.util.ResetModel} via its parent class.
 */
// TODO: make this a singleton injectable for player and enemy
// TODO: consider making this a Glazed List?
public class CharacterContainer<T extends Character> extends DisposableObject implements Iterable<T> {
    private final Set<T> objects;

    public CharacterContainer(EventBus eventBus, Set<T> objects) {
        super(eventBus);
        this.objects = objects;
    }

    public void add(T object) {
        objects.add(object);
    }

    public Set<T> getObjects() {
        return objects;
    }

    // TODO: type erasure make this listens to all Disposed events, can we optimize?
    @Subscribe
    public void objectDispose(Disposed<T> disposed) {
        objects.remove(disposed.getObject());
    }

    @Override
    protected void disposed() {
        objects.clear();
        super.disposed();
    }

    @Override
    public Iterator<T> iterator() {
        return objects.iterator();
    }
}
