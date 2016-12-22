package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.jingyuyao.tactical.model.Player;

import java.util.Observable;

public class PlayerActor extends CharacterActor<Player> {
    PlayerActor(Player object, float size, Sprite sprite, Color tint, EventListener listener) {
        super(object, size, sprite, tint, listener);
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
