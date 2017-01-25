package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.event.Attack;
import com.jingyuyao.tactical.model.character.event.InstantMove;
import com.jingyuyao.tactical.model.character.event.Move;
import com.jingyuyao.tactical.model.character.event.RemoveSelf;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.mark.Marking;
import java.util.Map;

public class CharacterActor<T extends Character> extends MapActor<T> {

  private static final float TIME_PER_UNIT = 0.06f; // time to move across one world unit in seconds

  private final Sprite sprite;

  CharacterActor(
      T object,
      EventListener listener,
      float size,
      Map<Marker, Sprite> markerSpriteMap,
      Sprite sprite) {
    super(object, listener, size, markerSpriteMap);
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
  public void removeSelf(RemoveSelf removeSelf) {
    remove();
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

  @Subscribe
  public void attacked(final Attack attack) {
    final Marking marking = attack.getObject().createHitMarking();

    Runnable showHitMarker = new Runnable() {
      @Override
      public void run() {
        marking.apply();
      }
    };
    Runnable hideHitMarker = new Runnable() {
      @Override
      public void run() {
        marking.clear();
      }
    };

    SequenceAction sequence = Actions.sequence(
        Actions.run(showHitMarker),
        Actions.delay(0.25f),
        Actions.run(hideHitMarker),
        Actions.delay(0.1f),
        Actions.run(showHitMarker),
        Actions.delay(0.2f),
        Actions.run(hideHitMarker),
        Actions.run(
            new Runnable() {
              @Override
              public void run() {
                attack.done();
              }
            }));
    addAction(sequence);
  }

  private SequenceAction getMoveSequence(Iterable<Coordinate> track) {
    SequenceAction sequence = Actions.sequence();
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
    clearListeners();
    return builder.build();
  }
}
