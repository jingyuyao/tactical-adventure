package com.jingyuyao.tactical.model.character;

import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.map.TargetsFactory;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.state.MapState;
import java.util.List;
import javax.inject.Inject;

/**
 * An enemy character
 */
public class Enemy extends Character {

  @Inject
  Enemy(
      EventBus eventBus,
      TargetsFactory targetsFactory,
      @Assisted Coordinate coordinate,
      @InitialMarkers List<Marker> markers,
      @Assisted String name,
      @Assisted Stats stats,
      @Assisted Items items) {
    super(eventBus, coordinate, markers, name, stats, items, targetsFactory);
  }

  @Override
  public void select(MapState mapState) {
    mapState.select(this);
  }
}
