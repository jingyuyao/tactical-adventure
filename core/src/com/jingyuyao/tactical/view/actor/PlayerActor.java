package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.Waiter;
import com.jingyuyao.tactical.model.event.NewActionState;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.object.Player;

import java.util.Map;

class PlayerActor extends CharacterActor<Player> {
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

    @Subscribe
    public void newActionState(NewActionState newActionState) {
        if (getObject().equals(newActionState.getPlayer())) {
            setColor(newActionState.isActionable() ? Color.WHITE : Color.GRAY);
        }
    }
}
