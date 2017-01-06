package com.jingyuyao.tactical.view.actor;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.Waiter;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.event.CharacterDied;
import com.jingyuyao.tactical.model.character.event.InstantMove;
import com.jingyuyao.tactical.model.character.event.Move;
import com.jingyuyao.tactical.model.mark.Marker;
import java.util.List;
import java.util.Map;

class CharacterActor<T extends Character> extends BaseActor<T> {

  private static final float TIME_PER_UNIT = 0.06f; // time to move across one world unit in seconds

  private final Waiter waiter;
  private final Sprite sprite;

  CharacterActor(
      T object,
      EventListener listener,
      float size,
      EventBus eventBus,
      Waiter waiter,
      Map<Marker, Sprite> markerSpriteMap,
      List<Sprite> markerSprites,
      Sprite sprite,
      Color initialTint) {
    super(object, listener, size, eventBus, markerSpriteMap, markerSprites);
    this.waiter = waiter;
    this.sprite = sprite;
    setColor(initialTint);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    if (sprite != null) {
      sprite.setColor(getColor());
      sprite.setBounds(getX(), getY(), getWidth(), getHeight());
      sprite.draw(batch);
    }
    super.draw(batch, parentAlpha);
  }

  @Subscribe
  public void instantMoveTo(InstantMove instantMove) {
    if (instantMove.matches(getObject())) {
      Coordinate destination = instantMove.getDestination();
      setPosition(destination.getX(), destination.getY());
    }
  }

  @Subscribe
  public void moveTo(Move move) {
    if (move.matches(getObject())) {
      final ImmutableList<EventListener> listeners = popAllListeners();
      SequenceAction moveSequence = getMoveSequence(move.getPath().getTrack());
      moveSequence.addAction(
          run(
              new Runnable() {
                @Override
                public void run() {
                  for (EventListener listener : listeners) {
                    addListener(listener);
                  }
                  waiter.finishOne();
                }
              }));
      waiter.waitOne();
      addAction(moveSequence);
    }
  }

  @Subscribe
  public void characterDied(CharacterDied characterDied) {
    if (characterDied.matches(getObject())) {
      remove();
    }
  }

  private SequenceAction getMoveSequence(Iterable<Coordinate> track) {
    SequenceAction sequence = sequence();
    for (Coordinate terrain : track) {
      sequence.addAction(Actions.moveTo(terrain.getX(), terrain.getY(), TIME_PER_UNIT));
    }
    return sequence;
  }

  private ImmutableList<EventListener> popAllListeners() {
    ImmutableList.Builder<EventListener> builder = new ImmutableList.Builder<EventListener>();
    for (EventListener listener : getListeners()) {
      builder.add(listener);
    }
    getListeners().clear();
    return builder.build();
  }
}
