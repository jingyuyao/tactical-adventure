package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.controller.InputLock;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.character.event.NewActionState;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.view.actor.ActorAnnotations.ActorWorldSize;
import com.jingyuyao.tactical.view.actor.ActorAnnotations.InitialMarkerSprites;
import com.jingyuyao.tactical.view.actor.ActorAnnotations.InitialPlayerTint;
import com.jingyuyao.tactical.view.actor.ActorAnnotations.PlayerSprite;
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
      Map<Marker, Sprite> markerSpriteMap,
      @InitialMarkerSprites List<Sprite> markerSprites,
      @PlayerSprite Sprite sprite,
      @InitialPlayerTint Color initialTint,
      InputLock inputLock) {
    super(object, listener, size, eventBus, markerSpriteMap, markerSprites, sprite, initialTint,
        inputLock);
  }

  @Subscribe
  public void newActionState(NewActionState newActionState) {
    if (newActionState.matches(getObject())) {
      setColor(newActionState.isActionable() ? Color.WHITE : Color.GRAY);
    }
  }
}
