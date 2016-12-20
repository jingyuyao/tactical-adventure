package com.jingyuyao.tactical.model;

import com.google.common.base.Objects;

import java.util.Collection;

/**
 * Manages selection logic.
 */
// TODO: This class needs to be thoroughly tested
public class Selector {
    private final Map map;
    private final Selections selections;
    /**
     * State transition should be handled by methods like {@link #goToWaitingState()}
     */
    private State state;

    Selector(Map map, Selections selections) {
        this.map = map;
        this.selections = selections;
        state = State.WAITING;
    }

    void select(Player player) {
        switch (state) {
            case WAITING:
                handleWaiting(player);
                break;
            case MOVING:
                handleMoving(player);
                break;
            case TARGETING:
                handleTargeting(player);
                break;
        }
    }

    void select(Enemy enemy) {
        switch (state) {
            case WAITING:
                handleWaiting(enemy);
                break;
            case MOVING:
                handleMoving(enemy);
                break;
            case TARGETING:
                handleTargeting(enemy);
                break;
        }
    }

    void select(Terrain terrain) {
        switch (state) {
            case WAITING:
                // Do nothing
                break;
            case MOVING:
                map.moveIfAble(selections.getSelectedPlayer(), terrain);
                if (map.hasAnyTarget(selections.getSelectedPlayer())) {
                    goToTargetingState();
                } else {
                    // TODO: go to action state
                    goToWaitingState();
                }
                break;
            case TARGETING:
                goToWaitingState();
                break;
        }
    }

    private void handleWaiting(Player player) {
        goToMovingState(player);
    }

    private void handleWaiting(Enemy enemy) {
        selections.selectedEnemy(enemy);
        goToWaitingState();
    }

    private void handleMoving(Player player) {
        if (Objects.equal(selections.getSelectedPlayer(), player)) {
            goToWaitingState();
        } else {
            goToMovingState(player);
        }
    }

    private void handleMoving(Enemy enemy) {
        Collection<Terrain> targetTerrains = map.getAllTargetTerrains(selections.getSelectedPlayer());
        Terrain enemyTerrain = map.get(enemy.getX(), enemy.getY());
        if (targetTerrains.contains(enemyTerrain)) {
            // TODO: Move character & enter battle prep
            goToWaitingState();
        } else {
            goToWaitingState();
        }
    }

    private void handleTargeting(Player player) {
        // TODO: cancel movement by retracing last path?
        goToWaitingState();
    }

    private void handleTargeting(Enemy enemy) {
        goToBattleState(enemy);
    }

    private void goToMovingState(Character playerCharacter) {
        selections.selectedPlayer(playerCharacter);
        state = State.MOVING;
    }

    private void goToWaitingState() {
        selections.selectedPlayer(null);
        state = State.WAITING;
    }

    private void goToTargetingState() {
        selections.showImmediateTargets();
        state = State.TARGETING;
    }

    private void goToBattleState(Character target) {
        // TODO: actually go to battle prep
        map.kill(target);
        goToWaitingState();
    }

    private enum State {
        WAITING,
        MOVING,
        TARGETING,
    }
}
