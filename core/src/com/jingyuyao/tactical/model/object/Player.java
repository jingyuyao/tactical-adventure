package com.jingyuyao.tactical.model.object;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.state.MapState;
import com.jingyuyao.tactical.model.util.ModelEvent;

/**
 * A player character
 */
public class Player extends Character {
    private boolean actionable = true;

    public Player(EventBus eventBus, int x, int y, String name, Stats stats, Items items) {
        super(eventBus, x, y, name, stats, items);
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

    public static class NewActionState implements ModelEvent {
        private final Player player;
        private final boolean actionable;

        private NewActionState(Player player, boolean actionable) {
            this.player = player;
            this.actionable = actionable;
        }

        public Player getPlayer() {
            return player;
        }

        public boolean isActionable() {
            return actionable;
        }
    }
}
