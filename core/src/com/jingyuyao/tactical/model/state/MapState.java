package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.Enemy;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.Player;
import com.jingyuyao.tactical.model.Terrain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;

/**
 * Manages selection logic.
 */
// TODO: This class needs to be thoroughly tested
public class MapState extends Observable {
    private final Collection<Action> actions;
    private State state;

    public MapState(Map map) {
        state = new Waiting(map, new Markings(map));
        this.actions = new ArrayList<Action>();
    }

    public Collection<Action> getActions() {
        return actions;
    }

    public void select(Player player) {
        changeState(state.select(player));
    }

    public void select(Enemy enemy) {
        changeState(state.select(enemy));
    }

    public void select(Terrain terrain) {
        changeState(state.select(terrain));
    }

    private void changeState(State newState) {
        actions.clear();
        actions.addAll(newState.getActions());
        state = newState;
        setChanged();
        notifyObservers();
    }
}
