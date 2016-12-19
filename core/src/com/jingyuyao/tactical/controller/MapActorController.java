package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.MapLogic;

/**
 * Only dispatches clicked events if the click begins and ends over the actor.
 */
abstract class MapActorController extends ClickListener {
    private final MapLogic mapLogic;

    MapActorController(MapLogic mapLogic, float actorSize) {
        this.mapLogic = mapLogic;
        setTapSquareSize(actorSize / 2f);
    }

    MapLogic getMapLogic() {
        return mapLogic;
    }
}
