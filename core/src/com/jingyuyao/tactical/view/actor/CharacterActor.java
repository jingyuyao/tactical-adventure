package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
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
import com.jingyuyao.tactical.view.resource.LoopAnimation;
import com.jingyuyao.tactical.view.resource.WorldTexture;
import java.util.LinkedHashSet;

class CharacterActor<T extends Character> extends WorldActor<T> {

  private final LoopAnimation loopAnimation;

  CharacterActor(
      T object, ActorConfig actorConfig, LinkedHashSet<WorldTexture> markers,
      LoopAnimation loopAnimation) {
    super(object, actorConfig, markers);
    this.loopAnimation = loopAnimation;
    object.registerListener(this);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    batch.setColor(getColor());
    loopAnimation.getCurrentFrame().draw(batch, this);
    batch.setColor(Color.WHITE);
    super.draw(batch, parentAlpha);
  }

  @Subscribe
  void instantMove(InstantMove instantMove) {
    updateCoordinate(instantMove.getDestination());
  }

  @Subscribe
  void move(final Move move) {
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
