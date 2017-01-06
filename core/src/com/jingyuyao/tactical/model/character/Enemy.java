package com.jingyuyao.tactical.model.character;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.character.event.InstantMove;
import com.jingyuyao.tactical.model.character.event.Move;
import com.jingyuyao.tactical.model.map.TargetsFactory;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.mark.Marking;
import com.jingyuyao.tactical.model.mark.MarkingFactory;
import com.jingyuyao.tactical.model.state.MapState;
import java.util.List;
import javax.inject.Inject;

/**
 * An enemy character
 */
public class Enemy extends Character {

  private final MarkingFactory markingFactory;
  private Marking dangerArea = null;

  @Inject
  Enemy(
      EventBus eventBus,
      @Assisted Coordinate coordinate,
      @InitialMarkers List<Marker> markers,
      @Assisted String name,
      @Assisted Stats stats,
      @Assisted Items items,
      TargetsFactory targetsFactory,
      MarkingFactory markingFactory) {
    super(eventBus, coordinate, markers, name, stats, items, targetsFactory);
    this.markingFactory = markingFactory;
    register();
  }

  @Override
  public void dispose() {
    if (dangerArea != null) {
      clearDangerArea();
    }
    unregister();
    super.dispose();
  }

  @Override
  public void select(MapState mapState) {
    mapState.select(this);
  }

  @Subscribe
  public void moved(Move move) {
    refreshDangerArea();
  }

  @Subscribe
  public void instantMoved(InstantMove instantMove) {
    refreshDangerArea();
  }

  public void toggleDangerArea() {
    if (dangerArea == null) {
      applyCurrentDangerArea();
    } else {
      clearDangerArea();
    }
  }

  private void refreshDangerArea() {
    if (dangerArea != null) {
      clearDangerArea();
      applyCurrentDangerArea();
    }
  }

  private void applyCurrentDangerArea() {
    dangerArea = markingFactory.danger(createTargets());
    dangerArea.apply();
  }

  private void clearDangerArea() {
    dangerArea.clear();
    dangerArea = null;
  }
}
