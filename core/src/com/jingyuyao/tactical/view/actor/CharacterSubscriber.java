package com.jingyuyao.tactical.view.actor;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.controller.InputLock;
import com.jingyuyao.tactical.model.character.event.InstantMove;
import com.jingyuyao.tactical.model.character.event.Move;
import com.jingyuyao.tactical.model.character.event.NewActionState;
import com.jingyuyao.tactical.model.common.Coordinate;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CharacterSubscriber {

  private static final float TIME_PER_UNIT = 0.06f; // time to move across one world unit in seconds

  private final Actors actors;
  private final InputLock inputLock;

  @Inject
  CharacterSubscriber(Actors actors, EventBus eventBus, InputLock inputLock) {
    this.actors = actors;
    this.inputLock = inputLock;
    eventBus.register(this);
  }

  @Subscribe
  public void instantMoveTo(InstantMove instantMove) {
    MapActor mapActor = actors.get(instantMove.getObject());
    Coordinate destination = instantMove.getDestination();
    mapActor.setPosition(destination.getX(), destination.getY());
  }

  @Subscribe
  public void moveTo(final Move move) {
    final MapActor mapActor = actors.get(move.getObject());
    final ImmutableList<EventListener> listeners = popAllListeners(mapActor);
    SequenceAction moveSequence = getMoveSequence(move.getPath().getTrack());
    moveSequence.addAction(
        run(
            new Runnable() {
              @Override
              public void run() {
                for (EventListener listener : listeners) {
                  mapActor.addListener(listener);
                }
                move.done();
                inputLock.unlock();
              }
            }));
    inputLock.lock();
    mapActor.addAction(moveSequence);
  }

  @Subscribe
  public void newActionState(NewActionState newActionState) {
    MapActor actor = actors.get(newActionState.getObject());
    actor.setColor(newActionState.isActionable() ? Color.WHITE : Color.GRAY);
  }

  private SequenceAction getMoveSequence(Iterable<Coordinate> track) {
    SequenceAction sequence = Actions.sequence();
    for (Coordinate terrain : track) {
      sequence.addAction(Actions.moveTo(terrain.getX(), terrain.getY(), TIME_PER_UNIT));
    }
    return sequence;
  }

  private ImmutableList<EventListener> popAllListeners(Actor actor) {
    ImmutableList.Builder<EventListener> builder = new ImmutableList.Builder<EventListener>();
    for (EventListener listener : actor.getListeners()) {
      builder.add(listener);
    }
    actor.clearListeners();
    return builder.build();
  }
}
