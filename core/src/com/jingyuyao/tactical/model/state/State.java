package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.Enemy;
import com.jingyuyao.tactical.model.Player;
import com.jingyuyao.tactical.model.Terrain;

interface State {
    State select(Player player);

    State select(Enemy enemy);

    State select(Terrain terrain);
}