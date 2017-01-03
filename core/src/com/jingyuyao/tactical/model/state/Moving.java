package com.jingyuyao.tactical.model.state;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Targets;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.mark.Markings;

import javax.inject.Inject;

class Moving extends AbstractPlayerState {
    private Coordinate previousCoordinate;

    @Inject
    Moving(EventBus eventBus, MapState mapState, Markings markings, StateFactory stateFactory, @Assisted Player player) {
        super(eventBus, mapState, markings, stateFactory, player);
    }

    @Override
    public void enter() {
        super.enter();
        getMarkings().showMoveAndTargets(getPlayer());
    }

    @Override
    public void canceled() {
        if (previousCoordinate != null) {
            getPlayer().instantMoveTo(previousCoordinate);
        }
    }

    @Override
    public void select(Player player) {
        if (Objects.equal(getPlayer(), player)) {
            goTo(getStateFactory().createChoosing(getPlayer()));
        } else {
            rollback();
            goTo(getStateFactory().createMoving(player));
        }
    }

    @Override
    public void select(final Enemy enemy) {
        Targets playerInfo = getPlayer().createTargetInfo();
        if (playerInfo.canHitAfterMove(enemy)) {
            Coordinate moveCoordinate = playerInfo.moveForTarget(enemy.getCoordinate());
            ImmutableList<Coordinate> path = playerInfo.pathTo(moveCoordinate);
            if (path.isEmpty()) {
                throw new RuntimeException("Shouldn't be possible");
            }

            moveCurrentPlayer(moveCoordinate, path);
            // creates an intermediate choosing state so we can backtrack here if needed
            goTo(getStateFactory().createChoosing(getPlayer()));
            goTo(getStateFactory().createSelectingWeapon(getPlayer(), enemy));
        } else {
            back();
        }
    }

    @Override
    public void select(Terrain terrain) {
        Targets playerInfo = getPlayer().createTargetInfo();
        if (playerInfo.canMoveTo(terrain.getCoordinate())) {
            ImmutableList<Coordinate> path = playerInfo.pathTo(terrain.getCoordinate());
            moveCurrentPlayer(terrain.getCoordinate(), path);
            goTo(getStateFactory().createChoosing(getPlayer()));
        } else {
            // we will consider clicking outside of movable area to be canceling
            back();
        }
    }

    @Override
    public ImmutableList<Action> getActions() {
        return ImmutableList.<Action>of(this.new Back());
    }

    private void moveCurrentPlayer(Coordinate destination, ImmutableList<Coordinate> path) {
        Player player = getPlayer();
        previousCoordinate = player.getCoordinate();
        player.moveTo(destination, path);
    }
}
