package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.Marker;
import com.jingyuyao.tactical.model.Waiter;
import com.jingyuyao.tactical.model.object.Player;

import java.util.Map;
import java.util.Observable;

public class PlayerActor extends CharacterActor<Player> {
    PlayerActor(
            EventBus eventBus,
            Player object,
            float size,
            Waiter waiter,
            Map<Marker, Sprite> markerSpriteMap,
            Sprite sprite,
            Color tint,
            EventListener listener
    ) {
        super(eventBus, object, size, waiter, markerSpriteMap, sprite, tint, listener);
    }

    @Override
    public void update(Observable observable, Object param) {
        super.update(observable, param);
        if (Player.NewActionState.class.isInstance(param)) {
            updateActionable(Player.NewActionState.class.cast(param));
        }
    }

    private void updateActionable(Player.NewActionState newActionState) {
        Color tint = newActionState.isActionable() ? Color.WHITE : Color.GRAY;
        getSprite().setColor(tint);
    }
}
