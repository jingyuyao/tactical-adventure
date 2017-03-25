package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.character.event.InstantMove;
import com.jingyuyao.tactical.model.character.event.Move;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.view.resource.LoopAnimation;
import com.jingyuyao.tactical.view.resource.WorldTexture;
import java.util.LinkedHashSet;

public class CharacterActor extends WorldActor {

  private final float moveTimePerUnit;
  private final LoopAnimation loopAnimation;

  CharacterActor(
      float moveTimePerUnit,
      LinkedHashSet<WorldTexture> markers,
      LoopAnimation loopAnimation) {
    super(markers);
    this.moveTimePerUnit = moveTimePerUnit;
    this.loopAnimation = loopAnimation;
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
    updateCoordinate(instantMove.getDestination().getCoordinate());
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

  private SequenceAction getMoveSequence(Iterable<Cell> track) {
    SequenceAction sequence = Actions.sequence();
    for (Cell cell : track) {
      sequence.addAction(createMoveToAction(cell.getCoordinate()));
    }
    return sequence;
  }

  private Action createMoveToAction(Coordinate coordinate) {
    return Actions.moveTo(
        coordinate.getX() * getWidth(), coordinate.getY() * getHeight(), moveTimePerUnit);
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
