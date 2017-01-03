package com.jingyuyao.tactical.model.state;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.common.EventObject;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.mark.Markings;

abstract class AbstractState extends EventObject implements State {
    private final MapState mapState;
    private final Markings markings;
    private final StateFactory stateFactory;

    AbstractState(EventBus eventBus, MapState mapState, Markings markings, StateFactory stateFactory) {
        super(eventBus);
        this.mapState = mapState;
        this.markings = markings;
        this.stateFactory = stateFactory;
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public void enter() {
        register();
    }

    @Override
    public void canceled() {

    }

    @Override
    public void exit() {
        unregister();
    }

    @Override
    public void select(Player player) {
        back();
    }

    @Override
    public void select(Enemy enemy) {
        back();
    }

    @Override
    public void select(Terrain terrain) {
        back();
    }

    StateFactory getStateFactory() {
        return stateFactory;
    }

    Markings getMarkings() {
        return markings;
    }

    void goTo(State newState) {
        mapState.push(newState);
    }

    void back() {
        mapState.pop();
    }

    void rollback() {
        mapState.rollback();
    }

    void finish(Player player) {
        player.setActionable(false);
        mapState.newStack(stateFactory.createWaiting());
    }

    class Back implements Action {
        @Override
        public String getName() {
            return "back";
        }

        @Override
        public void run() {
            back();
        }
    }
}
