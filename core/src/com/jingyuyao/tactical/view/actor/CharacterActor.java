package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.event.InstantMove;
import com.jingyuyao.tactical.model.character.event.Move;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.view.util.ViewUtil;
import java.util.LinkedHashSet;

class CharacterActor<T extends Character> extends WorldActor<T> {

  private final Sprite sprite;

  CharacterActor(T object, ActorConfig actorConfig, LinkedHashSet<Sprite> markers, Sprite sprite) {
    super(object, actorConfig, markers);
    this.sprite = sprite;
    object.registerListener(this);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    if (sprite != null) {
      sprite.setColor(getColor());
      ViewUtil.draw(batch, sprite, this);
    }
    super.draw(batch, parentAlpha);
  }

  @Subscribe
  public void instantMove(InstantMove instantMove) {
    updateCoordinate(instantMove.getDestination());
  }

  @Subscribe
  public void move(final Move move) {
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
      sequence.addAction(createMoveToAction(coordinate));
    }
    return sequence;
  }

  private Action createMoveToAction(Coordinate coordinate) {
    float size = getActorConfig().getActorWorldSize();
    float moveTimePerUnit = getActorConfig().getMoveTimePerUnit();
    return Actions.moveTo(coordinate.getX() * size, coordinate.getY() * size, moveTimePerUnit);
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
