package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.TargetInfo;

/**
 * A state with a selected player.
 */
abstract class AbstractPlayerState extends AbstractState {
    private final Player currentPlayer;
    private TargetInfo targetInfo;

    /**
     * Convenience method to transition between {@link AbstractPlayerState}s.
     */
    AbstractPlayerState(AbstractPlayerState prevState) {
        this(prevState, prevState.getCurrentPlayer());
    }

    AbstractPlayerState(AbstractState prevState, Player currentPlayer) {
        super(prevState);
        this.currentPlayer = currentPlayer;
    }

    Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Use after {@link #enter()}.
     */
    TargetInfo getTargetInfo() {
        return targetInfo;
    }

    @Override
    public void enter() {
        targetInfo = getTargetInfoFactory().create(currentPlayer);
    }

    @Override
    public void exit() {
        getMarkings().clearPlayerMarking();
    }
}
