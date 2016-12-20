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

    public void select(Character character) {
        // By using return state, we can let IDE check if we are missing any branches ;)
        switch (state) {
            case WAITING:
                handleWaiting(character);
                break;
            case MOVING:
                handleMoving(character);
                break;
            case TARGETING:
                handleTargeting(character);
                break;
        }
    }

    public void select(Terrain terrain) {
        switch (state) {
            case WAITING:
                // Do nothing
                break;
            case MOVING:
                map.moveIfAble(selections.getSelectedPlayer(), terrain);
                // TODO: check to go to action state or targeting state
                goToWaitingState();
                break;
            case TARGETING:
                goToWaitingState();
                break;
        }
    }

    private void handleWaiting(Character character) {
        switch (character.getType()) {
            case PLAYER:
                goToMovingState(character);
                break;
            case ENEMY:
                selections.selectedEnemy(character);
                goToWaitingState();
                break;
        }
    }

    private void handleMoving(Character character) {
        switch (character.getType()) {
            case PLAYER:
                if (Objects.equal(selections.getSelectedPlayer(), character)) {
                    goToWaitingState();
                } else {
                    goToMovingState(character);
                }
                break;
            case ENEMY:
                Collection<Terrain> attackTerrains = map.getAttackTerrains(selections.getSelectedPlayer());
                Terrain enemyTerrain = map.get(character.getX(), character.getY());
                if (attackTerrains.contains(enemyTerrain)) {
                    // TODO: Move character & enter battle prep
                    goToWaitingState();
                } else {
                    goToWaitingState();
                }
                break;
        }
    }

    private void handleTargeting(Character character) {
        switch (character.getType()) {
            case PLAYER:
                // TODO: cancel?
                goToWaitingState();
                break;
            case ENEMY:
                // TODO: enter battle prep
                goToBattlePrepState(character);
                break;
        }
    }

    private void goToMovingState(Character playerCharacter) {
        selections.selectedPlayer(playerCharacter);
        state = State.MOVING;
    }

    private void goToWaitingState() {
        selections.selectedPlayer(null);
        state = State.WAITING;
    }

    private void goToTargetingState(Character playerCharacter) {
        // TODO: finish me
        state = State.TARGETING;
    }

    private void goToBattlePrepState(Character target) {
        // TODO: finish me
        state = State.BATTLE_PREP;
    }

    private enum State {
        WAITING,
        MOVING,
        TARGETING,
        BATTLE_PREP,
    }
}
