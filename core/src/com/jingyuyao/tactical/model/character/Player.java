package com.jingyuyao.tactical.model.character;

import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.event.NewActionState;
import com.jingyuyao.tactical.model.map.TargetInfoFactory;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.state.MapState;

import javax.inject.Inject;
import java.util.List;

/**
 * A player character
 */
public class Player extends Character {
    private boolean actionable = true;

    @Inject
    Player(
            EventBus eventBus,
            TargetInfoFactory targetInfoFactory,
            @Assisted Coordinate coordinate,
            @InitialMarkers List<Marker> markers,
            @Assisted String name,
            @Assisted Stats stats,
            @Assisted Items items
    ) {
        super(eventBus, targetInfoFactory, coordinate, markers, name, stats, items);
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
        post(new NewActionState(this, actionable));
    }
}
