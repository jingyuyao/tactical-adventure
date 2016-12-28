package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.TargetInfo;
import com.jingyuyao.tactical.model.object.Player;

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
    void enter() {
        targetInfo = getTargetInfoFactory().createFor(currentPlayer);
    }
}
