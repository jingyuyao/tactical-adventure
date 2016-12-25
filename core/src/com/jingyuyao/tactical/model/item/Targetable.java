package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.object.Character;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;

/**
 * An {@link Item} that can be used on a target {@link Character}.
 */
public abstract class Targetable extends Usable {
    public Targetable(int id, String name, int usageLeft) {
        super(id, name, usageLeft);
    }

    public void targetAndUse(Character from, Character target) {
        performAction(from, target);
        usedOnce();
    }

    /**
     * Determines whether this {@link Targetable} can target {@code character}.
     * Useful to making items that can only target {@link Player} or {@link Enemy}.
     */
    public abstract boolean canTarget(Character from, Character to);

    protected abstract void performAction(Character from, Character to);
}
