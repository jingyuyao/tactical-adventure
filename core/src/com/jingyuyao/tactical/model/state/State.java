package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.action.Action;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;

public interface State {
    String getName();

    ImmutableList<Action> getActions();

    /**
     * Called when entering this state.
     *
     * Note: Constructor is unreliable for setting up new state data since we can go back to the previous state
     * which won't be re-instantiated.
     */
    void enter();

    /**
     * Called when this state is canceled by going to previous state.
     * Do NOT change state in this method.
     */
    void canceled();

    /**
     * Called when this state exits.
     */
    void exit();

    void select(Player player);

    void select(Enemy enemy);

    void select(Terrain terrain);
}
