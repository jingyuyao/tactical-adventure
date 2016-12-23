package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.jingyuyao.tactical.model.AnimationCounter;
import com.jingyuyao.tactical.model.Player;

import java.util.Observable;

public class PlayerActor extends CharacterActor<Player> {
    PlayerActor(
            Player object,
            float size,
            AnimationCounter animationCounter,
            Sprite sprite,
            Color tint,
            EventListener listener
    ) {
        super(object, size, animationCounter, sprite, tint, listener);
    }

    @Override
    public void update(Observable observable, Object o) {
        super.update(observable, o);
        updateActionable();
    }

    private void updateActionable() {
        Color tint = getObject().isActionable() ? Color.WHITE : Color.GRAY;
        getSprite().setColor(tint);
    }
}
