package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.Enemy;
import com.jingyuyao.tactical.model.Player;
import com.jingyuyao.tactical.model.Terrain;

public interface SelectionState {
    SelectionState select(Player player);

    SelectionState select(Enemy enemy);

    SelectionState select(Terrain terrain);
}
