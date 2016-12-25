package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.object.Character;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;

/**
 * An {@link Item} that can be used on a target {@link Character}.
 */
public abstract class Targetable extends Usable {
    private Character target;

    public Targetable(int id, String name, int usageLeft) {
        super(id, name, usageLeft);
    }

    public void setTarget(Character target) {
        this.target = target;
    }

    public void targetAndUse(Character character) {
        setTarget(character);
        use();
    }

    @Override
    protected void used() {
        if (target != null) {
            useOnTarget(target);
        }
    }

    /**
     * Determines whether this {@link Targetable} can target {@code character}.
     * Useful to making items that can only target {@link Player} or {@link Enemy}.
     */
    public abstract boolean canTarget(Character character);

    /**
     * The code to run when this item is used on {@code character}.
     */
    protected abstract void useOnTarget(Character character);
}
