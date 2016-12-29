package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.Terrain;

public interface State {
    String getName();

    ImmutableList<Action> getActions();

    void select(Player player);

    void select(Enemy enemy);

    void select(Terrain terrain);
}
