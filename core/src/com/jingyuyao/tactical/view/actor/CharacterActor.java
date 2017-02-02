package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multiset;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.event.InstantMove;
import com.jingyuyao.tactical.model.character.event.Move;
import com.jingyuyao.tactical.model.map.Coordinate;

public class CharacterActor<T extends Character> extends MapActor<T> {

  private static final float TIME_PER_UNIT = 0.06f; // time to move across one world unit in seconds

  private final Sprite sprite;

  CharacterActor(
      T object,
      EventListener listener,
      float size,
      Multiset<Sprite> markerSprites,
      Sprite sprite) {
    super(object, listener, size, markerSprites);
    this.sprite = sprite;
    object.registerListener(this);
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
    Coordinate destination = instantMove.getDestination();
    setPosition(destination.getX(), destination.getY());
  }

  @Subscribe
  public void moveTo(final Move move) {
    final ImmutableList<EventListener> listeners = popAllListeners();
    SequenceAction moveSequence = getMoveSequence(move.getPath().getTrack());
    moveSequence.addAction(
        Actions.run(
            new Runnable() {
              @Override
              public void run() {
                for (EventListener listener : listeners) {
                  addListener(listener);
                }
                move.done();
              }
            }));
    addAction(moveSequence);
  }

  private SequenceAction getMoveSequence(Iterable<Coordinate> track) {
    SequenceAction sequence = Actions.sequence();
    for (Coordinate coordinate : track) {
      sequence.addAction(Actions.moveTo(coordinate.getX(), coordinate.getY(), TIME_PER_UNIT));
    }
    return sequence;
  }

  private ImmutableList<EventListener> popAllListeners() {
    ImmutableList.Builder<EventListener> builder = ImmutableList.builder();
    for (EventListener listener : getListeners()) {
      builder.add(listener);
    }
    clearListeners();
    return builder.build();
  }
}
