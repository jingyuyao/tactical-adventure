package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.Enemy;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.Player;
import com.jingyuyao.tactical.model.Terrain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;

abstract class AbstractState extends Observable {
    private final Map map;
    private final Markings markings;
    private final Collection<Action> actions;
    private AbstractState transitionTarget;

    AbstractState(AbstractState prevState) {
        this(prevState.getMap(), prevState.getMarkings());
    }

    AbstractState(Map map, Markings markings) {
        this.map = map;
        this.markings = markings;
        this.actions = new ArrayList<Action>();
    }

    public Collection<Action> getActions() {
        return actions;
    }

    Map getMap() {
        return map;
    }

    Markings getMarkings() {
        return markings;
    }

    AbstractState getTransitionTarget() {
        return transitionTarget;
    }

    void transitionTo(AbstractState newState) {
        transitionTarget = newState;
        setChanged();
        notifyObservers();
    }

    AbstractState waiting() {
        return new Waiting(this);
    }

    abstract AbstractState select(Player player);

    abstract AbstractState select(Enemy enemy);

    abstract AbstractState select(Terrain terrain);
}
