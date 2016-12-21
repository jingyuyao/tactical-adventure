package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.Enemy;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.Player;
import com.jingyuyao.tactical.model.Terrain;

/**
 * Manages selection logic.
 */
// TODO: This class needs to be thoroughly tested
public class MapState {
    private State state;

    public MapState(Map map) {
        state = new Waiting(map, new StateData(map));
    }

    public void select(Player player) {
        state = state.select(player);
    }

    public void select(Enemy enemy) {
        state = state.select(enemy);
    }

    public void select(Terrain terrain) {
        state = state.select(terrain);
    }
}
