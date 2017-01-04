package com.jingyuyao.tactical.model.character;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.character.event.NewActionState;
import com.jingyuyao.tactical.model.map.TargetsFactory;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.state.MapState;
import com.jingyuyao.tactical.model.state.Waiting;

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
            TargetsFactory targetsFactory,
            @Assisted Coordinate coordinate,
            @InitialMarkers List<Marker> markers,
            @Assisted String name,
            @Assisted Stats stats,
            @Assisted Items items
    ) {
        super(eventBus, targetsFactory, coordinate, markers, name, stats, items);
        register();
    }

    @Subscribe
    public void endTurn(Waiting.EndTurn endTurn) {
        setActionable(true);
    }

    @Override
    public void dispose() {
        super.dispose();
        unregister();
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
