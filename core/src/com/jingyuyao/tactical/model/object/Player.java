package com.jingyuyao.tactical.model.object;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.event.NewActionState;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.state.MapState;

import java.util.List;

/**
 * A player character
 */
public class Player extends Character {
    private boolean actionable = true;

    Player(EventBus eventBus, Coordinate coordinate, List<Marker> markers, String name, Stats stats, Items items) {
        super(eventBus, coordinate, markers, name, stats, items);
    }

    @Override
    public void select(MapState mapState) {
        mapState.select(this);
    }

    public boolean isActionable() {
        return actionable;
    }

    public void setActionable(boolean actionable) {
        this.actionable = actionable;
        getEventBus().post(new NewActionState(this, actionable));
    }
}
