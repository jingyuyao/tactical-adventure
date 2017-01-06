package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.Waiter;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.character.event.NewActionState;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.view.actor.ActorConfig.ActorWorldSize;
import com.jingyuyao.tactical.view.actor.ActorConfig.InitialMarkerSprites;
import com.jingyuyao.tactical.view.actor.ActorConfig.InitialPlayerTint;
import com.jingyuyao.tactical.view.actor.ActorConfig.PlayerSprite;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

// Note: Guice reflection needs all injectable classes to be public
public class PlayerActor extends CharacterActor<Player> {

  @Inject
  PlayerActor(
      @Assisted Player object,
      @Assisted EventListener listener,
      @ActorWorldSize float size,
      EventBus eventBus,
      Waiter waiter,
      Map<Marker, Sprite> markerSpriteMap,
      @InitialMarkerSprites List<Sprite> markerSprites,
      @PlayerSprite Sprite sprite,
      @InitialPlayerTint Color initialTint) {
    super(object, listener, size, eventBus, waiter, markerSpriteMap, markerSprites, sprite,
        initialTint);
  }

  @Subscribe
  public void newActionState(NewActionState newActionState) {
    if (newActionState.matches(getObject())) {
      setColor(newActionState.isActionable() ? Color.WHITE : Color.GRAY);
    }
  }
}
